package user;

import lib.GeneralHelperFunctions;
import lib.payment.VisaCard;
import product.*;
import sun.java2d.loops.FillRect;

import java.util.Scanner;

public class BasicUser implements User {
	private long accountNumber;
	private static int number_of_accounts = 1;

	private String username;
	private String accountType = "Basic user";

	private VisaCard card = new VisaCard();
	private Cart cart = new Cart(card);


	/* ============== CONSTRUCTORS ============== */
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


	/* ============== GETTERS ============== */
	long getAccountNumber() { return this.accountNumber; }
	String getUsername() { return this.username; }
	String getAccountType() { return this.accountType; }



	/* ============== PUBLIC METHODS ============== */
	public void addToCart(Product p) {
		cart.addToCart(p);
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

	public void userCartMenu() {
		this.cart.cartMenu();
	}
}
