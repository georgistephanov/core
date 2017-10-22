package user;

import data.MySQLAccess;
import lib.GeneralHelperFunctions;

public class UserFactory {

	public static User createUserObject() {
		User user;

		//System.out.println("\nDo you have an account? (y/n)");

		//if (GeneralHelperFunctions.askForDecision()) {

			MySQLAccess db = MySQLAccess.getMySQLObject();
			String username = UserOperations.initUser();

			if (db.isAdmin(username)) {
				user = new Admin(username);
			} else if (db.isManager(username)) {
				user = new Manager(username);
			} else if (db.isPremium(username)) {
				user = new BasicUser(username, true);
			} else {
				user = new BasicUser(username, false);
			}

		//} else {
		//	user = new BasicUser();
		//}

		return user;
	}

}