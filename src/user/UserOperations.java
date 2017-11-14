package user;

import core.Engine;
import data.ProductDatabase;
import data.UserDatabase;
import lib.SystemDiagnostics;
import java.util.ArrayList;

// TODO: Leave here only common methods for two or more classes. Everything else goes to its own class

final class UserOperations {
	private static UserDatabase userDatabase = new UserDatabase();
	private static int LOGIN_MAX_TRY = 3;

	private UserOperations() {}


	/* ========= AUTHORISATION METHODS ========= */
	static String initUser() {
		if (LOGIN_MAX_TRY > 0) {
			String username = getUsernameFromInput();
			String password = getPasswordFromInput();

			if (userDatabase.login(username, password)) {
				return username;
			} else {
				LOGIN_MAX_TRY--;
				System.out.println("\n\n" + LOGIN_MAX_TRY + " tries left.");
				initUser();
			}
		} else {
			Engine.getInstance().terminateApplication();
		}

		// Unreachable statement
		return "";
	}
	static String createUser() {
		String username;
		String password;
		ArrayList<String> usernames = userDatabase.getUsernameList();
		boolean usernameExists;

		System.out.println("\nCreating a new account...");

		do {
			usernameExists = false;

			System.out.print("\nUsername: ");
			username = getUsernameFromInput();

			for (String u : usernames) {
				if (username.equals(u)) {
					System.out.println("Username already exists! Please pick another one.");
					usernameExists = true;
				}
			}
		} while (usernameExists);

		System.out.print("Password: ");
		password = getPasswordFromInput();

		if (password != null) {

			if ( new UserDatabase().registerUser(username, password) ) {
				System.out.println("\n\nThank you for becoming a part of Giorgio's.\n");
				return username;
			}
			else {
				System.out.println("(BasicUser: BasicUser()) Unable to register user.");
				Engine.getInstance().terminateApplication();
			}

		} else {
			System.out.println("Invalid username and/or password.");
			Engine.getInstance().terminateApplication();
		}

		return null;
	}
	static void changePassword(User b) {
		System.out.print("Enter old password: ");
		String oldPassword = Engine.getInputScanner().next();

		if (userDatabase.passwordMatch(b.getUsername(), oldPassword)) {
			System.out.print("Enter new password: ");
			String newPassword = Engine.getInputScanner().next();

			if (!newPassword.equals(oldPassword)) {
				if (userDatabase.changePassword(b.getUsername(), newPassword)) {
					System.out.println("Password changed successfully!");
				} else {
					System.out.println("The userDatabase failed while storing the password. Please try again in a few moments.");
				}
			} else {
				System.out.println("Your new password can't match the current one.");
			}
		} else {
			System.out.println("Passwords don't match.");
		}
	}

	/* ========= CARD METHODS ========= */
	static void addNewCard(User b) {
		System.out.println("\nAdd a card number (XXXX-XXXX-XXXX-XXXX)");
		String cardNum = Engine.getInputScanner().next();
		Engine.flushInputScanner();

		// Checks the first of two possible input formats
		if (cardNum.length() == 16) {
			if (userDatabase.addCard(b.getID(), cardNum)) {
				System.out.println("The card has been added successfully.");
			} else {
				System.out.println("There was an error while establishing connection to the userDatabase. Please try again in a few moments.");
			}
		} else if (cardNum.length() == 19) {
			String card_num[] = cardNum.split("-");

			if (card_num.length == 4) {
				cardNum = card_num[0] + card_num[1] + card_num[2] + card_num[3];

				if (cardNum.length() == 16) {
					if (userDatabase.addCard(b.getID(), cardNum)) {
						System.out.println("The card has been added successfully.");
					} else {
						System.out.println("There was an error while establishing connection to the userDatabase. Please try again in a few moments.");
					}
				}
			}
		} else {
			System.out.println("\nPlease enter the number in the correct format.");
		}
	}
	static void printCardInformation(User b) {
		System.out.println(b.getCard().toString());
	}

	/* ========= ORDER RELATED METHODS ========= */
	static void printPreviousOrders(User u) {
		userDatabase.printPreviousOrders(u.getID());
	}
	static void printFullPreviousOrder() {
		System.out.print("Order number: ");
		int orderNumber = Engine.getInputScanner().nextInt();
		Engine.getInputScanner().nextLine();

		userDatabase.printFullPreviousOrder(orderNumber);
	}

	/* ========= GENERAL METHODS ========= */
	static void searchProductByName() {
		System.out.print("\n\tEnter search query: ");

		String searchQuery = Engine.getInputScanner().nextLine();
		System.out.println("Search query: " + searchQuery);

		new ProductDatabase().printProductSuggestionsFromSearchQuery(searchQuery);
	}
	static void printSystemInformation() {
		SystemDiagnostics.getInstance().printSystemInformation();
	}

	/* ======== PRIVATE METHODS ======== */
	static void changeFirstName(User u) {
		System.out.println("What is your first name?");
		String newFirstName = Engine.getInputScanner().next();

		if (userDatabase.changeFirstName(u.getID(), newFirstName)) {
			u.updateName();
		}
	}
	static void changeLastName(User u) {
		System.out.println("What is your last name?");
		String newLastName = Engine.getInputScanner().next();

		if (userDatabase.changeLastName(u.getID(), newLastName)) {
			u.updateName();
		}
	}
	private static String getUsernameFromInput() {
		System.out.print("Username: ");
		return Engine.getInputScanner().next();
	}
	private static String getPasswordFromInput() {
		System.out.print("Password: ");
		return Engine.getInputScanner().next();
	}

}
