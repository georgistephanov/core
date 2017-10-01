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
		cart.addToCart(p);
	}

	public boolean checkout() {
		//TODO: reduce the quantity of the successfully checked out products
		return UserOperations.makeCheckoutPayment(this, cart);
	}

	public void scanProduct() throws InputMismatchException {
		Scanner scanner = new Scanner(System.in);
		long prodID;

		System.out.println("Type in the product ID: ");
		prodID = scanner.nextLong();

		Product p = ProductCatalog.productAvailable(prodID);

		if (p != null) {
			cart.addToCart(p);
			System.out.println("Product '" + p.getName() + "' successfully added to the cart.");
		}
		else {
			System.out.println("Ooops... There is no product with this ID in our catalog.");
		}
	}

}
