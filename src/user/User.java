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
	protected long account_num = 100_123_00;
	protected String username;

	public String accountType;
	public boolean authorised = false;

	protected VisaCard card = new VisaCard();
	protected Cart cart = new Cart(card, false);

	protected MySQLAccess db = MySQLAccess.getMySQLObject();

	/* ============== ABSTRACT CLASSES ============== */
	public abstract void initialiseMainMenu();
	public abstract void userProfileMenu();
	//public abstract void userCartMenu();



	/* ============== IMPLEMENTED CLASSES ============== */

	// Method which sets the object variables when constructed
	protected void setObjectVariables(String username, String accountType) {
		this.username = username;
		account_num += MySQLAccess.getMySQLObject().getIDFromUsername(username);
		this.accountType = accountType;
		authorised = true;
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
				if (!cart.empty()) {
					if (cart.checkout()) {
						initialiseMainMenu();
						break;
					}
				} else {
					System.out.println("The cart is empty.");
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

}
