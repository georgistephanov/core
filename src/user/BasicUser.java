package user;

import core.Engine;
import data.UserDatabase;


public class BasicUser extends User {

	/* ============== CONSTRUCTORS ============== */

	// Main constructor which is used when an unregistered user is using the program
	BasicUser() {
		String[] info = UserOperations.createUser();;

		if (info[0] != null && info[1] != null) {
			if (database.registerUser(info[0], info[1])) {
				setObjectVariables(info[0], User.AccountType.BASIC);
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
			setObjectVariables(username, User.AccountType.PREMIUM);
		} else {
			setObjectVariables(username, User.AccountType.BASIC);
		}
	}

	/* ============== IMPLEMENTED ABSTRACT METHODS ============== */
	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}

}
