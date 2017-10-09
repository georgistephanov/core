package user;

import lib.GeneralHelperFunctions;
import product.*;
import sun.java2d.loops.FillRect;

import java.util.Scanner;

public class BasicUser implements User {
	private long accountNumber;
	private static int number_of_accounts = 1;

	private String username;
	private String accountType = "Basic user"; //TODO: this should be enumeration

	private Cart cart = new Cart();

	// CONSTRUCTORS
	public BasicUser() {
		accountNumber = User.account_num + BasicUser.number_of_accounts++;
		String[] info = UserOperations.createUser();
		this.username = info[0];
		// TODO: store the password
	}
	public BasicUser(String username, String password) {
		UserOperations.login(username, password);
		accountNumber = User.account_num + BasicUser.number_of_accounts++;
		this.username = username;
	}

	// Getters
	long getAccountNumber() { return this.accountNumber; }
	String getUsername() { return this.username; }
	String getAccountType() { return this.accountType; }

	// PUBLIC METHODS
	public void addToCart(Product p) {
		System.out.println("Product '" + p.getName() + "' is available.\nDo you want to add it to your cart? (y/n)");

		if (GeneralHelperFunctions.askForDecision()) {
			System.out.println("Product '" + p.getName() + "' successfully added to the cart.");
			cart.addToCart(p);
		}
		else {
			System.out.println("The product has not been added to the cart.");
		}

		//TODO Check to see if the product is in the cart now. If so, ask whether to delete it or not.
	}

	public boolean checkout() {
		//TODO: reduce the quantity of the successfully checked out products
		//TODO: Encapsulate the call to UserOprations.checkout
		return UserOperations.checkout(this, cart);
	}

	public void userProfileMenu() {
		System.out.println("\nProfile:");
		System.out.println("\t1. View profile info");
		System.out.println("\t2. Edit profile");
		System.out.println("\n\t0. Back");

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
			default:
				return;
		}
	}

}
