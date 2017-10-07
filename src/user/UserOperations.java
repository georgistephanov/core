package user;
import lib.payment.*;
import product.*;

abstract class UserOperations {

	static String[] createUser() {
		// TODO: modify + add validation logic
		String[] info = new String[2];

		java.util.Scanner scanner = new java.util.Scanner(System.in);
		System.out.print("Create a new account...\n\nUsername: ");
		info[0] = scanner.next();

		System.out.print("Password: ");
		info[1] = scanner.next();

		System.out.println("Username: " + info[0] + "   Password: " + info[1]);
		return info;
	}

	static boolean login(String user, String pw) {
		// TODO: needs to be implemented
		return true;
	}

	static double getFunds(User user) {
		return user.card.getBalance();
	}

	static boolean makeCheckoutPayment(User user, Cart c) {
		return makePayment(user, c.getTotalAmount());
	}


	// PRIVATE METHODS
	private static boolean makePayment(User user, double amount) {
		if (user.card.makePayment(amount)) {
			System.out.println("Payment successful! New balance: $" + user.card.getBalance());
			return true;
		}
		else {
			System.out.println("Payment unsuccessful! Insufficient amount of money in the card.");
			return false;
		}
	}
}
