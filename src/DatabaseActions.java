import java.sql.*;
import java.util.ArrayList;
import com.mysql.cj.jdbc.MysqlDataSource;



	// Establishes database connection
	public DatabaseActions() {
		
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
}