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

		System.out.println("Create a new account...");

		do {
			usernameExists = false;

			System.out.println("\nUsername: ");
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

		System.out.println("\nUsername: " + info[0] + "   Password: " + info[1]);
		return info;
	}

	// TODO: needs to be implemented after the createUser method
	static boolean login(String user, String pw) {
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (db.login(user, pw)) {
			return true;
		}
		return false;
	}

	static double getFunds(User user) {
		return user.card.getBalance();
	}

	static void printProfileInfo(BasicUser b) {
		System.out.println("Account number:\t" + b.getAccountNumber());
		System.out.println("Username:\t\t" + b.getUsername());
		System.out.println("Account type:\t" + b.getAccountType());
	}

	static void editProfile(BasicUser b) {

	}
}
