package database;
import java.util.ArrayList;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDatabaseActions {

	// Establishes database connection
	public UserDatabaseActions() {
		
		createConnection();		
	}
	
	// Closes database connection
	protected void finalize() {
		
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
		} 
		
		catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Close the database connection
	private void closeConnection() {	
		try {
			conn.close();
		} 
		
		catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Retrieves all users email_address from the users table and returns as an ArrayList
	// Most likely not needed for the application but included for testing purposes
	public ArrayList<String> getEmails()
	{
		ArrayList<String> emails = new ArrayList<String>();
		try {
			final Statement stmt = conn.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT email_address FROM users");
			
			while(rs.next()) {
				final String email = rs.getString("email_address");
				emails.add(email);
			}
			stmt.close();
		} 
		
		catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return emails;
	}
	
	// Pass in a user's email address and return whether they are an authorized (insurance or maintainer) user type
	// Probably not good enough as-is and will need further code to ensure it only returns true if the user is authorized in their particular location
	public boolean isAuthorized(String email) {
			
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT account_type FROM users WHERE email_address = '" + email + "'");
			rs.next();
			final String type = rs.getString("account_type");
			if (type == "insurance" || type == "maintainer")
				return true;
			return false;
		} 
		
		catch (final SQLException e) {
			// Necessary for when a non-existent user/email is attempted
			return false;
		}	
	}

	// Used to create a new user
	public boolean createNewUser(final String email, final String displayName, final String password, final String accountType) {
		
		// Insert a new row into users table using the provided parameters
		try {
			Statement stmt = conn.createStatement();
			/* Likely need more columns in users table for String location and bool approved
			 * Location will be state the user is based in (used for authorized user to know what location they have privileges in)
			 * Approved will be true or false based on whether the State has approved their privileged access
			 * I'm not sure how it should work for insurance companies/other now but I will think about that another time.
			 */
			stmt.execute("INSERT INTO users(email_address, displayName, account_type) VALUES ('" + email + "', '" + displayName + "', '" + accountType + "')");
		}
		
		catch(final SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		// Hash and set the user's password
		if (setPassword(email, password))
			return true;
		
		return false;
	}
	
	// Used when setting a password for a new user
	private boolean setPassword(final String email, final String newPassword) {
		
		String hashedPassword;
		
		// Hash new password
		try {
			hashedPassword = hashString(newPassword);
		}
		catch(final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		// Update user's row in database with their hashed password
		try {
			final Statement stmt = conn.createStatement();
			stmt.execute("UPDATE users SET password_hash = '" + hashedPassword +"' WHERE email_address = '" + email + "'");
		}
		
		catch(final SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	// Used when an existing user wishes to reset an existing password. Must enter old password for validation
	public boolean resetPassword(final String email, final String oldPasswordUser, final String newPassword) {

		String hashedOldPasswordDb;
		String hashedOldPasswordUser;
		String hashedNewPassword;
		Statement stmt;
		
		// Hash old password entered from user
		try {
			hashedOldPasswordUser = hashString(oldPasswordUser);
		}
		catch(final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		// Retrieve the user's old password hash from database
		try {
			stmt = conn.createStatement();
			final ResultSet rs = stmt.executeQuery("SELECT password_hash FROM users WHERE email_address = '" + email + "'");
			rs.next();
			hashedOldPasswordDb = rs.getString("password_hash");
		}
		
		catch(final SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
		// Ensure the user entered their old password correctly before allowing reset
		if (!hashedOldPasswordUser.equals(hashedOldPasswordDb))
			return false;
		
		// Hash new password
		try {
			hashedNewPassword = hashString(newPassword);
		}
		catch(final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		// Update database with new password hash
		try {
			stmt.execute("UPDATE users SET password_hash = '" + hashedNewPassword +"' WHERE email_address = '" + email + "';");
			return true;
		}
		
		catch(final SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	// Update user's email
	public boolean changeEmail(String oldEmail, String newEmail) {
		
		try {
			Statement stmt = conn.createStatement();
			stmt.execute("UPDATE users SET email_address = '" + newEmail + "' WHERE email_address = '" + oldEmail + "'");
		} 
		
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}
	
	private String hashString(final String toHash) throws NoSuchAlgorithmException {
		
		// Hash text string
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(toHash.getBytes());
		final byte[] bytes = md.digest();
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < bytes.length; i++) {
	        sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	    }
		
		return sb.toString();
		
	}
}