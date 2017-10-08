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

	// TODO: needs to be implemented after the createUser method
	static boolean login(String user, String pw) {
		return true;
	}

	static double getFunds(User user) {
		return user.card.getBalance();
	}

	static boolean checkout(User user, Cart c) {
		if (user.card.makePayment(c.getTotalAmount())) {
			System.out.println("Payment successful! New balance: $" + user.card.getBalance());
			return true;
		}
		else {
			System.out.println("Payment unsuccessful! Insufficient amount of money in the card.");
			return false;
		}
	}
}
