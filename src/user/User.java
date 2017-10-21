package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import lib.payment.VisaCard;
import product.Product;

public abstract class User {
	protected long account_num = 100_123_00;
	protected String username;

	public String accountType;
	public boolean authorised = false;

	protected VisaCard card = new VisaCard();
	protected Cart cart = new Cart(card, false);


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
				if (!cart.empty() && cart.checkout()) {
					if (cart.checkout()) {
						return;
					}
					else {
						System.out.println("Unsuccessful checkout.");
					}
				}
				else
					System.out.println("The cart is empty.");

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
