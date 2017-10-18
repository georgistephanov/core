package user;

import core.Engine;
import lib.GeneralHelperFunctions;

public class Manager extends User {

	public Manager(String username) {
		System.out.println("Manager");
		setObjectVariables(username, "Manager");
	}

	// TODO: This could be well expanded
	public int initialiseMainMenu() {
		String mainMenu[] = {"Menu: ", "View catalog", "Profile", "System settings", "Exit"};
		GeneralHelperFunctions.generateMenu(mainMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				return 1;
			default:
				Engine.terminateApplication();
		}

		return -1;
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
