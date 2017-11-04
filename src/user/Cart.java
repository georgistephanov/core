package user;

import data.UserDatabase;
import lib.GeneralHelperFunctions;
import product.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import lib.payment.*;


class Cart {
	private ArrayList<Product> items;
	private User associatedUser;
	private int _discountPercentage;


	Cart(User user) {
		items = new ArrayList<>();
		associatedUser = user;
	}


	/* ============== PUBLIC METHODS ============== */

	// This method is responsible for all the logic regarding adding a product to the cart
	boolean addToCart(Product p) {

		// Checks if the product is already in the cart
		if (p.getQuantityAvailable() > 0) {
			System.out.println("Product '" + p.getName() + "' is available.");

			if (!_productAlreadyInCart(p)) {
				System.out.println("Do you want to add it to your cart? (y/n)");

				if (GeneralHelperFunctions.askForDecision()) {

					items.add(p);
					_increaseQuantity(p, 1);
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
						_increaseQuantity(p, quantity);
					}
				} else {
					System.out.println("Do you want to remove it from the cart? (y/n)");

					if (GeneralHelperFunctions.askForDecision()) {
						_removeFromCart(p);
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



	/* ============== PROTECTED METHODS ============== */

	// Prints a brief information about the products in the cart
	void showItems() {
		_printCheckoutConfirmation();
	}

	// Cancels the current line and removes the products from the cart
	void cancelLine() {
		if (_isEmpty()) {
			System.out.println("\nYour cart is empty.");
			return;
		}

		System.out.println("Are you sure you want to remove all the items from the cart? (y/n)");
		if (GeneralHelperFunctions.askForDecision()) {
			items = new ArrayList<>();
			System.out.println("\nYour cart has been emptied successfully");
		}
	}

	// Checks if the cart is empty
	private boolean _isEmpty() {
		return items.size() <= 0;
	}




	/* ============== PRIVATE METHODS ============== */

	// Checks whether the product passed as a parameter is in the cart
	private boolean _productAlreadyInCart(Product p) {
		for (Product i : items) {
			if (i.getID() == p.getID())
				return true;
		}

		return false;
	}

	// This method encapsulates the logic which increases the quantity of a product
	private void _increaseQuantity(Product p, int q) {
		if (_productAlreadyInCart(p))
			p.increaseQuantityInCart(q);
	}

	// Removes a product from the cart
	private void _removeFromCart(Product p) {
		if (items.remove(p)) {
			System.out.println(p.getName() + " successfully removed from the cart");
			p.productRemovedFromCart();
		} else {
			System.out.println("This item was not in the cart.");
		}
	}

	// Returns the total cost of all products in the cart
	private double _getTotalAmount() {
		double totalAmount = 0;

		if (!items.isEmpty()) {
			for (Product i : items) {
				totalAmount += i.getQuantityInCart() * i.getPrice();
			}
		}

		if (_discountPercentage >= 0 && _discountPercentage <= 90)
			totalAmount = totalAmount * (100 - _discountPercentage) / 100;

		return (double) Math.round(totalAmount * 100) / 100;
	}



	/* ============== CHECKOUT METHODS ============== */

	// For checkout logic refer to CORE Cashless on your phone's notes
	// TODO: After checkout store the purchase info + receipt in the database
	// TODO: This shall be done after the database structure is re-done

	// TODO: Decouple this from the user and the card. Let the payment be called from the user
	// The method responsible for all the logic regarding the checkout process
	boolean checkout(int discountPercentage) {
		if (associatedUser.getCard() == null) {
			System.out.println("There is no card associated with this account. Please add a card from the profile tab in order to proceed with the checkout");
			return false;
		}
		if (_isEmpty()) {
			System.out.println("The cart is empty");
			return false;
		}

		this._discountPercentage = discountPercentage;

		_printCheckoutConfirmation();
		System.out.println("Do you want to process the order?");

		if (GeneralHelperFunctions.askForDecision()) {
			if (_processOrder()) {
				items = new ArrayList<>();
				return true;
			}
		} else
			System.out.println("Canceling checkout...");


		return false;
	}

	private boolean _processOrder() {

		boolean paymentSuccessful;

		paymentSuccessful = associatedUser.getCard().makePayment(_getTotalAmount());


		if (paymentSuccessful) {
			System.out.println("Payment successful! New balance: $" + associatedUser.getCard().getBalance());
			_generateReceipt();
			_addOrderInformationToDatabase();
			return true;
		}

		return false;
	}

	private void _addOrderInformationToDatabase() {
		new UserDatabase().addOrder(associatedUser, _getTotalAmount(), items);
	}

	// Prints a confirmation message with all the info needed for a checkout
	private void _printCheckoutConfirmation() {
		System.out.println("\n|Qty| Price\t\t| Item\t");
		for (Product i : items) {
			System.out.println("| " + i.getQuantityInCart() + " | $" + i.getPrice() + (i.getPrice() < 10 ? "\t\t| " : "\t| ") + i.getName());
		}

		if (_discountPercentage == 0) {
			System.out.println("\nTotal: $" + _getTotalAmount());
		} else {
			System.out.println("\nTotal after " + _discountPercentage + "% discount: $" + _getTotalAmount());
		}

		System.out.println();
	}

	// Prints the receipt and stores it in the database
	// TODO: Store the receipt in the database
	private void _generateReceipt() {
		// TODO: Make it look like a real receipt
		_printCheckoutConfirmation();
		System.out.println("The transaction has been made successfully.\nYou may take the products with you!");
		System.out.println("Thank you for shopping at Giorgio's! Have a good day! :)");
	}
}