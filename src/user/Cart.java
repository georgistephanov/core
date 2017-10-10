package user;
import lib.GeneralHelperFunctions;
import product.*;
import java.util.ArrayList;

public class Cart {

	ArrayList<Product> items;
	double totalAmount;

	Cart() {
		items = new ArrayList<>();
		totalAmount = 0;
	}

	public void addToCart(Product p) {
		items.add(p);
		totalAmount += p.getPrice();
	}

	private void showItems() {
		if (items.size() > 0) {
			for (Product i : items) {
				i.printShortProductInfo();
			}
			System.out.println("\nYour cart has been emptied successfully");
		}
		else {
			System.out.println("\nYour cart is empty.");
		}
	}

	private void removeAllProducts() {
		if (!this.empty()) {
			System.out.println("Are you sure you want to remove all the items from the cart? (y/n)");
			if (GeneralHelperFunctions.askForDecision()) {
				items = new ArrayList<>();
			}
		}
		else {
			System.out.println("\nYour cart is empty.");
		}
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void cartMenu() {
		System.out.println("\nCart:");
		System.out.println("\t1. View products");
		System.out.println("\t2. Checkout");
		System.out.println("\t");
		System.out.println("\t9. Remove all products");
		System.out.println("\t0. Back");

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				showItems();
				break;
			case 2:
				checkout();
				break;
			case 9:
				removeAllProducts();
				break;
			case 0:
				return;
			default:

		}

		cartMenu();
	}

	// For checkout logic refer to CORE Cashless on your phone's notes
	private boolean checkout() {

		return false;
	}

	private boolean empty() {
		if (items.size() <= 0)
			return true;

		return false;
	}
}

// These should go inside the cart class (Menu option 4. Cart)
// TODO: Generate receipt
// TODO: Print before checkout [ Qty | Item | Price ]
// TODO: Process Order as a process from the Checkout
// TODO: Cancel line