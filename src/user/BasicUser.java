package user;

import core.Engine;
import core.PhysicalScanner;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import product.Product;
import product.ProductCatalog;
import sun.java2d.loops.FillRect;

import java.util.InputMismatchException;


public class BasicUser extends User {

	/* ============== CONSTRUCTORS ============== */

	// Main constructor which is used when an unregistered user is using the program
	public BasicUser() {
		String[] info = UserOperations.createUser();
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (info[0] != null && info[1] != null) {
			if (db.registerUser(info[0], info[1])) {
				setObjectVariables(info[0], "Basic");
			} else {
				System.out.println("(BasicUser: BasicUser()) Unable to register user.");
				Engine.terminateApplication();
			}
		} else {
			System.out.println("Invalid username and/or password.");
			Engine.terminateApplication();
		}
	}

	public BasicUser(String username, Boolean premium) {
		if (premium) {
			setObjectVariables(username, "Premium");
		} else {
			setObjectVariables(username, "Basic");
		}
	}

	/* ============== IMPLEMENTED ABSTRACT METHODS ============== */
	// TODO: Find a way to initialise common menu options for all users in the User class and
	// TODO:   then just add the rest of it in each of the classes!
	public void initialiseMainMenu() {
		// General menu
		String mainMenu[] = {"Menu:", "View catalog", "Scan product", "Profile", "Cart", "Exit"};
		GeneralHelperFunctions.generateMenu(mainMenu);


		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				ProductCatalog.printCatalog();
				break;
			case 2:
				Product p = PhysicalScanner.getInstance().scanProduct();
				if ( p != null) {
					getCart().addToCart(p);
				}
				break;
			case 3:
				userProfileMenu();
				break;
			case 4:
				userCartMenu();
				break;
			case 0:
				Engine.terminateApplication();
			case -1:
				return;
			default:
				throw new InputMismatchException();
		}
	}

	// TODO: Add options to add and remove cards from the account
	public void userProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile", "Change password", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 2);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(this);
				break;
			case 2:
				UserOperations.editProfile(this);
				break;
			case 3:
				UserOperations.changePassword(this);
				break;
			case -1:
				System.out.println("Incorrect input! Please try again.");
				userProfileMenu();
			case 0:
				initialiseMainMenu();
			default:
				break;
		}

		userProfileMenu();
	}

}
