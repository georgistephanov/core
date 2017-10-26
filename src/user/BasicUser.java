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
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}


}
