package user;
import lib.GeneralHelperFunctions;
import product.*;
import java.util.ArrayList;
import lib.payment.*;

public class Cart {

	ArrayList<Product> items;
	double totalAmount;
	Card associatedCard;

	Cart(Card card) {
		items = new ArrayList<>();
		totalAmount = 0;
		associatedCard = card;
	}

	public boolean addToCart(Product p) {
		if (p.getQuantityAvailable() > 0) {
			if (!productAlreadyInCart(p)) {
				items.add(p);
				totalAmount += p.getPrice();
				p.addedToCart();
			} else {
				System.out.println("The product is already in the cart. Would you like to add more of it?");
				if (GeneralHelperFunctions.askForDecision()) {
					p.addedToCart();
				}
			}
			return true;
		}

		return false;
	}

	private boolean productAlreadyInCart(Product p) {
		for (Product i : items) {
			// TODO: check the IDs as it would be safer
			if (i.getName() == p.getName())
				return true;
		}

		return false;
	}

	private void showItems() {
		if (items.size() > 0) {
			for (Product i : items) {
				i.printShortProductInfo();
			}
		}
		else {
			System.out.println("\nYour cart is empty.");
		}
	}

	private void cancelLine() {
		if (!this.empty()) {
			System.out.println("Are you sure you want to remove all the items from the cart? (y/n)");
			if (GeneralHelperFunctions.askForDecision()) {
				items = new ArrayList<>();
				System.out.println("\nYour cart has been emptied successfully");
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
		System.out.println("\t1. View items");
		System.out.println("\t2. Checkout");
		System.out.println("\t");
		System.out.println("\t9. Cancel line");
		System.out.println("\t0. Back");

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				showItems();
				break;
			case 2:
				if (!empty())
					checkout();
				else
					System.out.println("The cart is empty.");
				break;
			case 9:
				cancelLine();
				break;
			case 0:
				return;
			default:

		}

		cartMenu();
	}

	// For checkout logic refer to CORE Cashless on your phone's notes
	// TODO: After checkout store the purchase info + receipt in the database
	// TODO: Leave all the hard work to a method called Process Order
	private void checkout() {
		// TODO: Print before checkout [ Qty | Item | Price ]
		printCheckoutConfirmation();
		System.out.println("Do you want to process the order?");

		if (GeneralHelperFunctions.askForDecision())
			processOrder();
		else
			System.out.println("Going back...");
	}

	private void processOrder() {
		if (associatedCard.makePayment(getTotalAmount())) {
			System.out.println("Payment successful! New balance: $" + associatedCard.getBalance());
			generateReceipt();
		}
		else {
			System.out.println("Payment unsuccessful! Insufficient amount of money in the card.");
		}
	}

	private void printCheckoutConfirmation() {
		System.out.println("|Qty| Price\t\t| Item\t");
		for (Product i : items) {
			// Map the quantity in the cart
			System.out.println("| " + 1 + " | $" + i.getPrice() + (i.getPrice() < 10 ? "\t\t| " : "\t| ") + i.getName());
		}
		System.out.println();
	}

	private void generateReceipt() {
		// TODO: Make it look like a real receipt
		printCheckoutConfirmation();
		System.out.println("The transaction has been made successfully.\nYou may take the products with you!");
		System.out.println("Thank you for shopping at Giorgio's! Have a good day! :)");
	}

	private boolean empty() {
		if (items.size() <= 0)
			return true;

		return false;
	}
}

// These should go inside the cart class (Menu option 4. Cart)
// TODO: Process Order as a process from the Checkout