package user;

import data.MySQLAccess;
import lib.payment.VisaCard;

public abstract class User {
	private int id;

	private long account_num = 100_123_00;
	private String username;
	private String firstName, lastName;

	private String accountType;
	private boolean authorised = false;

	private VisaCard card;
	private Cart cart;

	private static MySQLAccess db = MySQLAccess.getMySQLObject();

	/* ============== STATIC FACTORY METHOD ============== */
	public static User createUserInstance() {
		//System.out.println("Do you have an account? (y/n)");
		//if (GeneralHelperFunctions.askForDecision()) {
			String username = UserOperations.initUser();

			if (db.isAdmin(username)) {
				return new Admin(username);
			} else if (db.isManager(username)) {
				return new Manager(username);
			} else if (db.isPremium(username)) {
				return new BasicUser(username, true);
			} else {
				return new BasicUser(username, false);
			}
		//} else {
			//return new BasicUser();
		//}
	}

	/* ============== ABSTRACT CLASSES ============== */
	public abstract void initialiseMainMenu();
	//public abstract void userProfileMenu();



	/* ============== IMPLEMENTED CLASSES ============== */

	// Method which sets the object variables when constructed
	protected void setObjectVariables(String username, String accountType) {
		id = db.getIDFromUsername(username);
		account_num += id;

		this.username = username;
		firstName = db.getFirstName(id);
		lastName = db.getLastName(id);
		System.out.println("\n\nHello, " + firstName);

		this.accountType = accountType;
		authorised = true;

		card = new VisaCard(id);
		cart = new Cart(card, (this.accountType.equalsIgnoreCase("premium") ? true : false));
		if (!card.isCardActive()) {
			System.out.println("There seems to be a missing credit card to this account. "
					+ "You won't be able to make any purchases until you add one!");
		}
	}

	protected void displayCartItems() {
		cart.showItems();
	}

	protected boolean beginCheckoutProcess() {
		return cart.checkout();
	}

	protected void removeItemsFromCart() {
		cart.cancelLine();
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
