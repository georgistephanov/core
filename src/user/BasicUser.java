package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import java.util.InputMismatchException;


public class BasicUser extends User {

	/* ============== CONSTRUCTORS ============== */

	// Main constructor which is used when an unregistered user is using the program
	public BasicUser() {
		String[] info = UserOperations.createUser();
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (info[0] != null && info[1] != null) {
			if (db.registerUser(info[0], info[1])) {
				setObjectVariables(info[0], "Basic");
			} else {
				System.out.println("(BasicUser: BasicUser()) Unable to register user.");
			}
		}
	}

	public BasicUser(String username) {
		setObjectVariables(username, "Basic");
	}


	/* ============== IMPLEMENTED ABSTRACT METHODS ============== */

	public int initialiseMainMenu() {
		// General menu
		String mainMenu[] = {"Menu:", "View catalog", "Scan product", "Profile", "Cart", "Exit"};
		GeneralHelperFunctions.generateMenu(mainMenu);


		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			case 0:
			case -1:
				Engine.terminateApplication();
			default:
				throw new InputMismatchException();
		}
	}

	public void userProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 2);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(this);
				break;
			case 2:
				UserOperations.editProfile(this);
				break;
			case -1:
				System.out.println("Incorrect input! Please try again.");
				userProfileMenu();
				break;
			case 0:
				return;
			default:
		}

		userProfileMenu();
	}

}
