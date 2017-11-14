package user;

import core.Engine;
import data.SystemDatabase;
import lib.GeneralHelperFunctions;

public class Admin extends User {

	Admin(String username) {
		setObjectVariables(username, User.AccountType.ADMIN);
	}

	public void initialiseMainMenu() {
		new UserMenu(this).initialiseMainMenu();
	}

	public String toString() { return super.toString(); }


	/* ======== PACKAGE-PRIVATE METHODS ======== */
	void viewUserInformation() {
		System.out.print("Enter user id or username: ");
		String query = Engine.getInputScanner().next();
		Engine.flushInputScanner();

		userDatabase.getUserInformationByUsernameOrID(query);
	}
	void giveUserPrivileges() {
		int id = getIdFromInput();

		String giveUserPrivileges[] = {"What privileges would you like to give them?", "Premium", "Manager", "Admin", "None"};
		GeneralHelperFunctions.generateMenu(giveUserPrivileges);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);
		switch (opt) {
			case 1:
				userDatabase.makeUserPremium(id);
				break;
			case 2:
				userDatabase.makeUserManager(id);
				break;
			case 3:
				userDatabase.makeUserAdmin(id);
				break;
			default:
				break;
		}
	}
	void deleteUser() {
		int id = getIdFromInput();
		userDatabase.deleteUser(id);
	}
	void printLastUserSessions() {
		userDatabase.printLastUserSessions();
	}
	void changeSystemVariables() {
		SystemDatabase systemDatabase = new SystemDatabase();

		if ( systemDatabase.printSystemVariablesAsMenu() ) {
			// Printed the menu with the variables and options as to which to change
			int opt = GeneralHelperFunctions.inputIntegerOption(0, 1);
			switch (opt) {
				case 1:
					systemDatabase.changeTestTimeRate();
					break;
				case 0:
					return;
				default:
					changeSystemVariables();
			}
		} else {
			// Didn't do anything due to userDatabase problems
			return;
		}
	}
	void clearTestsPerformed() { new SystemDatabase().clearTestsPerformed(); }
	void deleteUserSessions() { userDatabase.deleteAllSessions(); }


	/* ======== PRIVATE METHODS ======== */
	private static int getIdFromInput() {
		System.out.print("Enter user id: ");
		int id = Engine.getInputScanner().nextInt();
		Engine.flushInputScanner();

		return id;
	}
}
