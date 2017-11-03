package user;

import data.MySQLAccess;
import lib.payment.VisaCard;
import product.ProductCatalog;

public abstract class User {
	private int id;

	private long account_num = 100_123_00;
	private String username;
	private String firstName, lastName;

	public enum AccountType {
		BASIC(0), PREMIUM(10), MANAGER(15), ADMIN(0);

		private int _discountPercentage;

		AccountType(int discountPercentage) {
			this._discountPercentage = discountPercentage;
		}

		int getDiscountPercentage() { return _discountPercentage; }
	}

	private AccountType _accountType;

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


	/* ============== IMPLEMENTED CLASSES ============== */

	// Method which sets the object variables when constructed
	void setObjectVariables(String username, AccountType accountType) {
		id = db.getIDFromUsername(username);
		account_num += id;

		this.username = username;
		firstName = db.getFirstName(id);
		lastName = db.getLastName(id);

		this._accountType = accountType;
		authorised = true;

		card = new VisaCard(id);
		if (!card.isCardActive()) {
			System.out.println("There seems to be a missing credit card to this account. "
					+ "You won't be able to make any purchases until you add one!");
		}

		cart = new Cart(card);
	}

	void displayCartItems() {
		cart.showItems();
	}

	boolean checkout(int discountPercentage) {
		return cart.checkout(discountPercentage);
	}

	void removeItemsFromCart() {
		cart.cancelLine();
	}


	/* ============== GETTERS ============== */
	long getAccountNumber() { return this.account_num; }
	public String getUsername() { return this.username; }
	Cart getCart() { return this.cart; }
	VisaCard getCard() { return this.card; }
	public boolean isAuthorised() { return this.authorised; }
	AccountType getEnumAccountType() { return this._accountType; }
	String getStringAccountType() {
		switch (_accountType) {
			case BASIC:
				return "Basic";
			case PREMIUM:
				return "Premium";
			case MANAGER:
				return "Manager";
			case ADMIN:
				return "Admin";
			default:
				return "";
		}
	}
	String getFirstName() { return this.firstName; }
	String getLastName() { return this.lastName; }
	int getCheckoutDiscountPercentage() { return this._accountType.getDiscountPercentage(); }

	int getID() { return this.id; }


	// Print the catalog
	void printCatalog() {
		ProductCatalog.printCatalog();
	}

	// Updates the users' first and last name from the database
	void updateName() {
		firstName = db.getFirstName(id);
		lastName = db.getLastName(id);
	}

}
