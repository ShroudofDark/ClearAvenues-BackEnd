import java.util.ArrayList;

// This class used for testing purposes
public class main {

	public static void main(String[] args) {
		DatabaseActions dbConnection = new DatabaseActions();
		ArrayList<String> emails = dbConnection.getEmails();
		System.out.println("Emails:");
		for(String email : emails) {
			System.out.println("\t" + email);
		}
	}
}