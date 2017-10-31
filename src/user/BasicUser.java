package user;

import core.Engine;
import data.MySQLAccess;


public class BasicUser extends User {

	/* ============== CONSTRUCTORS ============== */

	// Main constructor which is used when an unregistered user is using the program
	BasicUser() {
		String[] info = UserOperations.createUser();
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (info[0] != null && info[1] != null) {
			if (db.registerUser(info[0], info[1])) {
				setObjectVariables(info[0], "Basic");
			} else {
				System.out.println("(BasicUser: BasicUser()) Unable to register user.");
				Engine.terminateApplication();
			}
		} else {
			System.out.println("Invalid username and/or password.");
			Engine.terminateApplication();
		}
	}

	// Constructor when a user exists and has successfully logged in
	BasicUser(String username, Boolean premium) {
		if (premium) {
			setObjectVariables(username, "Premium");
		} else {
			setObjectVariables(username, "Basic");
		}
	}

	/* ============== IMPLEMENTED ABSTRACT METHODS ============== */
	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}

}
