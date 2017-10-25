package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import lib.payment.VisaCard;
import product.Product;

// TODO: The class should be extended to hold other additional information
// TODO: about the person using it in order  to create a full profile.
// TODO: This shall be implemented after the user database has been re-done
public abstract class User {
	private int id;

	private long account_num = 100_123_00;
	private String username;
	private String firstName, lastName;

	private String accountType;
	private boolean authorised = false;

	private VisaCard card;
	private Cart cart;

	private MySQLAccess db = MySQLAccess.getMySQLObject();

	/* ============== ABSTRACT CLASSES ============== */
	public abstract void initialiseMainMenu();
	public abstract void userProfileMenu();
	//public abstract void userCartMenu();



	/* ============== IMPLEMENTED CLASSES ============== */

	// Method which sets the object variables when constructed
	protected void setObjectVariables(String username, String accountType) {
		// TODO: Refactor
		id = db.getIDFromUsername(username);

		this.username = username;
		account_num += id;

		firstName = db.getFirstName(id);
		lastName = db.getLastName(id);

		this.accountType = accountType;
		authorised = true;

		System.out.println("\n\nHello, " + firstName);

		card = new VisaCard(id);
		cart = new Cart(card, false);
		if (!card.isCardActive()) {
			System.out.println("There seems to be a missing credit card to this account. "
					+ "You won't be able to make any purchases until you add one!");
		}
	}

	// General cart menu interface
	public void userCartMenu() {
		String cartMenu[] = {"Cart:", "View items", "Checkout", "Cancel line", "Back"};
		GeneralHelperFunctions.generateMenu(cartMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				cart.showItems();
				break;
			case 2:
				if (card.isCardActive()) {
					if (!cart.empty()) {
						if (cart.checkout()) {
							initialiseMainMenu();
							break;
						}
					} else {
						System.out.println("The cart is empty.");
					}
				} else {
					//TODO: make this check in the checkout method of the cart
					System.out.println("There is no card associated with this account. Please add a card from the profile tab in order to proceed with the checkout");
				}

				break;
			case 3:
				cart.cancelLine();
				break;
			case 0:
				initialiseMainMenu();
			default:
				break;
		}

		userCartMenu();
	}


	/* ============== GETTERS ============== */
	public long getAccountNumber() { return this.account_num; }
	public String getUsername() { return this.username; }
	public String getAccountType() { return this.accountType; }
	public Cart getCart() { return this.cart; }
	public VisaCard getCard() { return this.card; }
	public boolean isAuthorised() { return this.authorised; }
	public String getFirstName() { return this.firstName; }
	public String getLastName() { return this.lastName; }

	protected int getID() { return this.id; }


	// Updates the users' first and last name from the database
	public void updateName() {
		firstName = db.getFirstName(id);
		lastName = db.getLastName(id);
	}

}
