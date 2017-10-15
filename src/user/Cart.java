package user;
import lib.GeneralHelperFunctions;
import product.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import lib.payment.*;

public class Cart {

	ArrayList<Product> items;
	double totalAmount;
	Card associatedCard;

	DecimalFormat f = new DecimalFormat("####.##");

	Cart(Card card) {
		items = new ArrayList<>();
		totalAmount = 0;
		associatedCard = card;
	}


	/* ============== PUBLIC METHODS ============== */

	// This method initialises the cart menu with all its menu options
	public void cartMenu() {
		String cartMenu[] = {"Cart:", "View items", "Checkout", "Cancel line", "Back"};
		GeneralHelperFunctions.generateMenu(cartMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				showItems();
				break;
			case 2:
				if (!empty())
					if (checkout())
						return;
					else
						System.out.println("The cart is empty.");
				break;
			case 3:
				cancelLine();
				break;
			case 0:
				return;
			default:
		}

		cartMenu();
	}


	// This method is responsible for all the logic regarding adding a product to the cart
	// TODO: REFACTOR THIS METHOD AS IT IS WAY TOO LONG
	public boolean addToCart(Product p) {

		if (p.getQuantityAvailable() > 0) {
			System.out.println("Product '" + p.getName() + "' is available.");

			if (!productAlreadyInCart(p)) {
				System.out.println("Do you want to add it to your cart? (y/n)");

				if (GeneralHelperFunctions.askForDecision()) {

					items.add(p);
					increaseQuantity(p, 1);
					System.out.println("Product '" + p.getName() + "' successfully added to the cart.");

				} else {
					System.out.println("The product has not been added to the cart.");
					return false;
				}

			} else {
				System.out.println("The product is already in the cart. Would you like to add more of it? (y/n)");

				if (GeneralHelperFunctions.askForDecision()) {

					System.out.println("Maximum quantity available: " + p.getQuantityAvailable());
					System.out.print("Enter quantity: ");
					int quantity = GeneralHelperFunctions.inputIntegerOption(0, p.getQuantityAvailable());

					if (quantity != -1) {
						increaseQuantity(p, quantity);
					}
				} else {
					System.out.println("Do you want to remove it from the cart? (y/n)");

					if (GeneralHelperFunctions.askForDecision()) {
						removeFromCart(p);
					}
				}
			}
			return true;

		} else {
			System.out.println("Insufficient quantity available for this product.");
			System.out.println("Please contact 02-123-345 for any further information whether this product will be available later on.");
			return false;
		}
	}



	/* ============== PRIVATE METHODS ============== */

	// Checks whether the product passed as a parameter is in the cart
	private boolean productAlreadyInCart(Product p) {
		for (Product i : items) {
			if (i.getID() == p.getID())
				return true;
		}

		return false;
	}

	// This method encapsulates the logic which increases the quantity of a product
	private void increaseQuantity(Product p, int q) {
		if (productAlreadyInCart(p))
			p.increaseQuantityInCart(q);
	}

	// Removes a product from the cart
	private void removeFromCart(Product p) {
		if (items.remove(p)) {
			System.out.println(p.getName() + " successfully removed from the cart");
			p.productRemovedFromCart();
		} else {
			System.out.println("This item was not in the cart.");
		}
	}

	// Prints a brief information about the products in the cart
	private void showItems() {
		if (items.size() > 0) {
			for (Product i : items) {
				i.printShortProductInfo();
			}
			System.out.println("Total: " + getTotalAmount());
		}
		else {
			System.out.println("\nYour cart is empty.");
		}
	}

	// Cancels the current line and removes the products from the cart
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

	// Returns the total cost of all products in the cart
	private double getTotalAmount() {
		double totalAmount = 0;

		if (!items.isEmpty()) {
			for (Product i : items) {
				totalAmount += i.getQuantityInCart() * i.getPrice();
			}
		}

		return (double) Math.round(totalAmount * 100) / 100;
	}

	// Checks if the cart is empty
	private boolean empty() {
		if (items.size() <= 0)
			return true;

		return false;
	}



	/* ============== CHECKOUT METHODS ============== */

	// For checkout logic refer to CORE Cashless on your phone's notes
	// TODO: After checkout store the purchase info + receipt in the database

	// The method responsible for all the logic regarding the checkout process
	private boolean checkout() {
		// TODO: Print before checkout [ Qty | Item | Price ]
		printCheckoutConfirmation();
		System.out.println("Do you want to process the order?");

		if (GeneralHelperFunctions.askForDecision())
			if (processOrder())
				return true;
		else
			System.out.println("Going back...");

		return false;
	}

	private boolean processOrder() {
		if (associatedCard.makePayment(getTotalAmount())) {
			System.out.println("Payment successful! New balance: $" + associatedCard.getBalance());
			generateReceipt();
			return true;
		}
		else {
			System.out.println("Payment unsuccessful! Insufficient amount of money in the card.");
			return false;
		}
	}

	// Prints a confirmation message with all the info needed for a checkout
	private void printCheckoutConfirmation() {
		System.out.println("\n|Qty| Price\t\t| Item\t");
		for (Product i : items) {
			System.out.println("| " + i.getQuantityInCart() + " | $" + i.getPrice() + (i.getPrice() < 10 ? "\t\t| " : "\t| ") + i.getName());
		}
		System.out.println("\nTotal: $" + getTotalAmount());
		System.out.println();
	}

	// Prints the receipt and stores it in the database
	// TODO: Store the receipt in the database
	private void generateReceipt() {
		// TODO: Make it look like a real receipt
		printCheckoutConfirmation();
		System.out.println("The transaction has been made successfully.\nYou may take the products with you!");
		System.out.println("Thank you for shopping at Giorgio's! Have a good day! :)");
	}
}