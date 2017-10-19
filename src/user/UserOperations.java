package user;
import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import lib.payment.*;
import product.*;

import java.util.ArrayList;

abstract class UserOperations {

	static String[] createUser() {
		String info[] = new String[2];
		ArrayList<String> usernames = MySQLAccess.getMySQLObject().getUsernames();
		boolean usernameExists;

		System.out.println("\nCreating a new account...");

		do {
			usernameExists = false;

			System.out.print("\nUsername: ");
			info[0] = Engine.inputScanner.next();

			for (String u : usernames) {
				if (info[0].equals(u)) {
					System.out.println("Username already exists! Please pick another one.");
					usernameExists = true;
				}
			}
		} while (usernameExists);

		System.out.print("Password: ");
		info[1] = Engine.inputScanner.next();

		System.out.println("\n\nThank you for becoming a part of Giorgio's.\n");
		return info;
	}

	static boolean login(String user, String pw) {
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (db.login(user, pw)) {
			return true;
		}
		return false;
	}

	static double getCurrentBalance(User user) {
		return user.card.getBalance();
	}

	static void printProfileInfo(User b) {
		System.out.println("Account number:\t" + b.getAccountNumber());
		System.out.println("Username:\t\t" + b.getUsername());
		System.out.println("Account type:\t" + b.getAccountType());
	}

	static void editProfile(User b) {
		System.out.println("\nDo you want to change your password?");

		if (GeneralHelperFunctions.askForDecision()) {
			changePassword(b);
		}
	}

	static boolean changePassword(User b) {
		System.out.print("Enter old password: ");
		String oldPassword = Engine.inputScanner.next();

		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (db.passwordMatch(b.getUsername(), oldPassword)) {
			System.out.print("Enter new password: ");
			String newPassword = Engine.inputScanner.next();

			if (!newPassword.equals(oldPassword)) {
				if (db.changePassword(b.getUsername(), newPassword)) {
					System.out.println("Password changed successfully!");
					return true;
				} else {
					System.out.println("The database failed while storing the password.");
					return false;
				}
			} else {
				System.out.println("Your new password can't match the current one.");
				return false;
			}
		} else {
			return false;
		}
	}


	// FACTORY METHODS
	static String[] askForUsernameAndPassword() {
		String info[] = new String[2];

		System.out.println("Username: ");
		info[0] = Engine.inputScanner.next();

		System.out.println("Password: ");
		info[1] = Engine.inputScanner.next();

		return info;
	}

	static int login_max_try = 3;

	static String initUser() {
		if (login_max_try > 0) {
			String info[] = askForUsernameAndPassword();

			if (UserOperations.login(info[0], info[1])) {
				return info[0];
			} else {
				login_max_try--;
				System.out.println("\n\n" + login_max_try + " tries left.");
				initUser();
			}
		} else {
			Engine.terminateApplication();
		}

		return "";
	}
}
