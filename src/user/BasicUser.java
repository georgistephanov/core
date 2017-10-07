package user;
import lib.payment.*;
import product.*;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class BasicUser implements User {
	public long accountNumber;
	static int number_of_accounts = 1;

	public String username;
	public String accountType = "Basic user"; //TODO: this could be enumeration

	public Cart cart = new Cart();

	// CONSTRUCTORS
	public BasicUser() {
		accountNumber = User.account_num + BasicUser.number_of_accounts++;
		String[] info = UserOperations.createUser();
		this.username = info[0];
	}
	public BasicUser(String username, String password) {
		UserOperations.login(username, password);
	}


	// PUBLIC METHODS
	public void addToCart(Product p) {
		System.out.println("Product '" + p.getName() + "' is available.\nDo you want to add it to the cart? (y/n)\n");

		Scanner s = new Scanner(System.in);
		String opt = s.nextLine();

		if (opt.charAt(0) == 'y' || opt.charAt(0) == 'Y') {
			System.out.println("Product '" + p.getName() + "' successfully added to the cart.");
			cart.addToCart(p);
		}
		else {
			System.out.println("The product has not been added to the cart.\n");
		}

		//TODO Check to see if the product is in the cart now. If so, ask whether to delete it or not.
	}

	public boolean checkout() {
		//TODO: reduce the quantity of the successfully checked out products
		return UserOperations.makeCheckoutPayment(this, cart);
	}

}
