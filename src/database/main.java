package database;
import java.util.ArrayList;

// This class used for testing purposes
public class main {

	public static void main(String[] args) {
		UserDatabaseActions dbConnection = new UserDatabaseActions();
		/* ArrayList<String> emails = dbConnection.getEmails();
		System.out.println("Emails:");
		for(String email : emails) {
			System.out.println("\t" + email);
		}*/
		
		//boolean authorized = dbConnection.isAuthorized("fm");
		//System.out.println(authorized);
		
		//dbConnection.createNewUser("mwils031@odu.edu_duplicate", "Matthew Wilson", "password1", "maintainer");
		dbConnection.resetPassword("mwils031@odu.edu", "password1", "test1");
	}
}