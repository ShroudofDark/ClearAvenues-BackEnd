package database;
import java.sql.*;
import java.util.ArrayList;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserDatabaseActions {

	// Establishes database connection
	public UserDatabaseActions() {
		
		createConnection();		
	}
	
	// Closes database connection
	private void finalize() {
		
		closeConnection();		
	}
	
	private MysqlDataSource dataSource;
	private Connection conn;
	
	// Sets up a MySQL DataSource to connect to the database as "java" user
	private void createConnection() {	
		dataSource = new MysqlDataSource();
		dataSource.setUser("java");
		dataSource.setPassword("Gr33n_T3am");
		dataSource.setServerName("clear-avenues-db.c45yztcmyatd.us-east-2.rds.amazonaws.com");
		
		try {
			conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute("use clear_avenues_db");
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Close the database connection
	private void closeConnection() {	
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Retrieves all users email_address from the users table and returns as an ArrayList
	public ArrayList<String> getEmails()
	{
		ArrayList<String> emails = new ArrayList<String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT email_address FROM users");
			
			while(rs.next())
			{
				String email = rs.getString("email_address");
				emails.add(email);
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return emails;
	}
	
	// Pass in a user's email address and return whether they are an authorized (insurance or maintainer) user type
	// Probably not good enough as-is and will need further code to ensure it only returns true if the user is authorized in their particular location
	public boolean isAuthorized(String email) {
			
		try {
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT account_type FROM users WHERE email_address = '" + email + "'");
			rs.next();
			String type = rs.getString("account_type");
			if (type == "insurance" || type == "maintainer")
				return true;
			return false;
		} catch (SQLException e) {
			// Necessary for when a non-existent user/email is attempted
			return false;
		}	
	}

	// Used to create a new user
	public boolean createNewUser(String email, String displayName, String password, String accountType) {
		
		Statement stmt;
		
		// Insert a new row into users table using the provided parameters
		try {
			stmt = conn.createStatement();
			/* Likely need more columns in users table for String location and bool approved
			 * Location will be state the user is based in (used for authorized user to know what location they have privileges in)
			 * Approved will be true or false based on whether the State has approved their privileged access
			 * I'm not sure how it should work for insurance companies/other now but I will think about that another time.
			 */
			stmt.execute("INSERT INTO users(email_address, displayName, account_type) VALUES ('" + email + "', '" + displayName + "', '" + accountType + "')");
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// Hash and set the user's password
		if (setPassword(email, password))
			return true;
		
		return false;
	}
	
	// Used when setting a password for a new user
	public boolean setPassword(String email, String newPassword) {
		
		String hashedPassword = null;
		
		// Hash new password
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(newPassword.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < bytes.length; i++) {
		        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
			
			hashedPassword = sb.toString();
		}
		
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		// Update user's row in database with their hashed password
		try {
			Statement stmt;
			stmt = conn.createStatement();
			stmt.execute("UPDATE users SET password_hash = '" + hashedPassword +"' WHERE email_address = '" + email + "'");
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// Used when an existing user wishes to reset an existing password. Must enter old password for validation
	public boolean resetPassword(String email, String oldPasswordUser, String newPassword) {

		String hashedNewPassword = null;
		String hashedOldPasswordUser = null;
		String hashedOldPasswordDb = null;
		
		// Hash old password entered from user
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(oldPasswordUser.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < bytes.length; i++) {
		        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
			
			hashedOldPasswordUser = sb.toString();
		}
		
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		Statement stmt;
		
		// Retrieve the user's old password hash from database
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT password_hash FROM users WHERE email_address = '" + email + "'");
			rs.next();
			hashedOldPasswordDb = rs.getString("password_hash");
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
		// Ensure the user entered their old password correctly before allowing reset
		if (!hashedOldPasswordUser.equals(hashedOldPasswordDb))
			return false;
		
		// Hash new password
		try {
			
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(newPassword.getBytes());
			byte[] bytes = md.digest();
			StringBuilder sb = new StringBuilder();
			
			for (int i = 0; i < bytes.length; i++) {
		        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		    }
			
			hashedNewPassword = sb.toString();
		}
		
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		// Update database with new password hash
		try {
			stmt.execute("UPDATE users SET password_hash = '" + hashedNewPassword +"' WHERE email_address = '" + email + "';");
			return true;
		}
		
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
}