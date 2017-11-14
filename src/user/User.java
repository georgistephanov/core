package user;

import core.Engine;
import data.UserDatabase;
import lib.GeneralHelperFunctions;
import lib.payment.VisaCard;
import product.ProductCatalog;
import java.util.HashMap;
import java.util.Map;


public abstract class User {
	private long account_num = 100_123_00;
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

	/**
	 * 	Type-safe heterogeneous container, which will hold one String
	 *	for the username and one Integer for the ID, so that it provides
	 *	global easy access to them.
	 */
	private class Information {
		private Map<Class<?>, Object> info = new HashMap<>();

		private <T> void putInformation(Class<T> classType, T instance) {
			info.put(classType, classType.cast(instance));
		}

		<T> T getInformation(Class<T> classType) {
			return classType.cast(info.get(classType));
		}
	}
	private Information information;

	private boolean authorised = false;

	private VisaCard card;
	private Cart cart;

	UserDatabase userDatabase = new UserDatabase();

	/* ============== STATIC FACTORY METHOD ============== */
	public static User createUserInstance() {
		UserDatabase staticDatabase = new UserDatabase();

		//System.out.println("Do you have an account? (y/n)");
		//if (GeneralHelperFunctions.askForDecision()) {
			String username = UserOperations.initUser();

			if (staticDatabase.isAdmin(username)) {
				return new Admin(username);
			} else if (staticDatabase.isManager(username)) {
				return new Manager(username);
			} else if (staticDatabase.isPremium(username)) {
				return new BasicUser(username, true);
			} else {
				return new BasicUser(username, false);
			}
		//} else {
			//return new BasicUser();
		//}
	}

	@Override public String toString() {
		return "\n============================="
				+ "\nAccount number:\t" 	+ this.account_num
				+ "\nUsername:\t\t"		+ getUsername()
				+ "\nAccount type:\t" 		+ this.getStringAccountType()
				+ "\n\nFirst name:\t\t" 		+ this.firstName
				+ "\nLast name:\t\t" 		+ this.lastName
				+ "\n=============================";
	}


	/* ============== ABSTRACT METHOD ============== */
	public abstract void initialiseMainMenu();


	/* ============== LOGOUT METHODS ============== */
	void logout() {
		System.out.println("Are you sure you want to logout?");
		if (GeneralHelperFunctions.askForDecision()) {
			this.authorised = false;
			GeneralHelperFunctions.printBlockMessage("You have successfully logged out.");
		}

		updateSessionUponLogout();
		Engine.getInstance().userLoggedOut();
	}
	public void hardLogout() {
		// This user logs out the user upon exit without asking for permission
		this.authorised = false;
		updateSessionUponLogout();
	}


	/* ============== CART METHODS ============== */
	void displayCartItems() {
		cart.showItems();
	}
	boolean checkout() {
		return cart.checkout();
	}
	void removeItemsFromCart() {
		cart.cancelLine();
	}


	/* ============== GETTERS ============== */
	String getUsername() { return information.getInformation(String.class); }
	public int getID() { return information.getInformation(Integer.class); }
	Cart getCart() { return this.cart; }
	VisaCard getCard() { return this.card; }
	public boolean isAuthorised() { return this.authorised; }
	private String getStringAccountType() {
		switch (_accountType) {
			case BASIC:
				return "Basic user";
			case PREMIUM:
				return "Premium user";
			case MANAGER:
				return "Manager";
			case ADMIN:
				return "Administrator";
			default:
				return "";
		}
	}
	public String getFirstName() { return this.firstName; }
	public int getCheckoutDiscountPercentage() { return this._accountType.getDiscountPercentage(); }


	/* ============== HELPER METHODS ============== */
	// Method which sets the object variables when constructed
	void setObjectVariables(String username, AccountType accountType) {

		int id = userDatabase.getIDFromUsername(username);
		account_num += id;

		information = new Information();
		information.putInformation(String.class, username);
		information.putInformation(Integer.class, id);

		firstName = userDatabase.getFirstName(id);
		lastName = userDatabase.getLastName(id);

		this._accountType = accountType;
		authorised = true;

		card = new VisaCard(id);
		if (!card.isCardActive()) {
			System.out.println("There seems to be a missing credit card to this account. "
					+ "You won't be able to make any purchases until you add one!");
		}

		cart = new Cart(this);
	}
	void printCatalog() {
		ProductCatalog.printCatalog();
	}
	void updateName() {
		firstName = userDatabase.getFirstName(getID());
		lastName = userDatabase.getLastName(getID());
	}
	private void updateSessionUponLogout() {
		userDatabase.logout(getID());
	}
}
