package user;

import core.Engine;
import data.UserDatabase;


public class BasicUser extends User {

	/* ============== CONSTRUCTORS ============== */

	// Main constructor which is used when an unregistered user is using the program
	BasicUser() {
		String username = UserOperations.createUser();
		setObjectVariables(username, User.AccountType.BASIC);
	}

	// Constructor when a user exists and has successfully logged in
	BasicUser(String username, Boolean premium) {
		if (premium) {
			setObjectVariables(username, User.AccountType.PREMIUM);
		} else {
			setObjectVariables(username, User.AccountType.BASIC);
		}
	}

	public String toString() {
		return super.toString() + "\n\nDiscount:\t" + getCheckoutDiscountPercentage() + "%";
	}

	/* ============== IMPLEMENTED ABSTRACT METHODS ============== */
	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}

}
