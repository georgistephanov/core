package user;

import core.Engine;
import data.MySQLAccess;
import lib.SystemDiagnostics;
import java.util.ArrayList;

final class UserOperations {
	private static int login_max_try = 3;

	private UserOperations() {}


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

	static void printProfileInfo(User u) {
		System.out.println("Account number:\t" + u.getAccountNumber());
		System.out.println("Username:\t\t" + u.getUsername());
		System.out.println("Account type:\t" + u.getAccountType());

		System.out.println("\nFirst name:\t" + u.getFirstName());
		System.out.println("Last name:\t" + u.getLastName());
	}

	static void changePassword(User b) {
		System.out.print("Enter old password: ");
		String oldPassword = Engine.inputScanner.next();

		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (db.passwordMatch(b.getUsername(), oldPassword)) {
			System.out.print("Enter new password: ");
			String newPassword = Engine.inputScanner.next();

			if (!newPassword.equals(oldPassword)) {
				if (db.changePassword(b.getUsername(), newPassword)) {
					System.out.println("Password changed successfully!");
				} else {
					System.out.println("The database failed while storing the password. Please try again in a few moments.");
				}
			} else {
				System.out.println("Your new password can't match the current one.");
			}
		} else {
			System.out.println("Passwords don't match.");
		}
	}

	static void addNewCard(User b) {
		System.out.println("\nAdd a card number (XXXX-XXXX-XXXX-XXXX)");
		String cardNum = Engine.inputScanner.next();
		Engine.inputScanner.nextLine();

		// Checks the first of two possible input formats
		if (cardNum.length() == 16) {
			if (MySQLAccess.getMySQLObject().addCard(b.getID(), cardNum)) {
				System.out.println("The card has been added successfully.");
			} else {
				System.out.println("There was an error while establishing connection to the database. Please try again in a few moments.");
			}
		} else if (cardNum.length() == 19) {
			String card_num[] = cardNum.split("-");

			if (card_num.length == 4) {
				cardNum = card_num[0] + card_num[1] + card_num[2] + card_num[3];

				if (cardNum.length() == 16) {
					if (MySQLAccess.getMySQLObject().addCard(b.getID(), cardNum)) {
						System.out.println("The card has been added successfully.");
					} else {
						System.out.println("There was an error while establishing connection to the database. Please try again in a few moments.");
					}
				}
			}
		} else {
			System.out.println("\nPlease enter the number in the correct format.");
		}
	}

	static void printCardInformation(User b) {
		b.getCard().printFullInformation();
	}

	static void makeUserPremium() {
		_makeUserPremium();
	}

	static void printPreviousOrders(User u) {
		MySQLAccess.getMySQLObject().printPreviousOrders(u.getID());
	}

	static void printFullPreviousOrder() {
		System.out.print("Order number: ");
		int orderNumber = Engine.inputScanner.nextInt();
		Engine.inputScanner.nextLine();

		MySQLAccess.getMySQLObject().printFullPreviousOrder(orderNumber);
	}

	static void printSystemInformation() {
		SystemDiagnostics.getInstance().printSystemInformation();
	}

	/* ======== PRIVATE METHODS ======== */

	static void changeFirstName(User u) {
		System.out.println("What is your first name?");
		String newFirstName = Engine.inputScanner.next();

		if (MySQLAccess.getMySQLObject().changeFirstName(u.getID(), newFirstName)) {
			u.updateName();
		}
	}

	static void changeLastName(User u) {
		System.out.println("What is your last name?");
		String newLastName = Engine.inputScanner.next();

		if (MySQLAccess.getMySQLObject().changeLastName(u.getID(), newLastName)) {
			u.updateName();
		}
	}

	private static void _makeUserPremium() {
		System.out.println("Enter user id: ");
		int id = Engine.inputScanner.nextInt();
		Engine.inputScanner.nextLine();

		if (id != 0) {
			if (id > 100_123_00)
				MySQLAccess.getMySQLObject().makeUserPremium(id - 100_123_00);
			else
				MySQLAccess.getMySQLObject().makeUserPremium(id);
		}
	}

	private static String getUsernameFromInput() {
		System.out.print("Username: ");
		return Engine.inputScanner.next();
	}

	private static String getPasswordFromInput() {
		System.out.print("Password: ");
		return Engine.inputScanner.next();
	}

	static String initUser() {
		if (login_max_try > 0) {
			String username = getUsernameFromInput();
			String password = getPasswordFromInput();

			if (MySQLAccess.getMySQLObject().login(username, password)) {
				return username;
			} else {
				login_max_try--;
				System.out.println("\n\n" + login_max_try + " tries left.");
				initUser();
			}
		} else {
			Engine.terminateApplication();
		}

		// Unreachable statement
		return "";
	}
}
