package core;
import lib.GeneralHelperFunctions;
import user.*;
import product.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

	public static void main(String[] args) {

		// These objects should be created inside the engine class
		Engine e = Engine.getInstance();
		e.execute();
	}
}
// Engine class which implements the Singleton pattern
class Engine {
	private static Engine e = new Engine();
	public boolean running;

	private BasicUser user;
	private PhysicalScanner scanner;
	private ProductCatalog db;

	private Engine() {
		running = true;

		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");

		user = new BasicUser("Georgi", "123");
		scanner = PhysicalScanner.initialise();
		db = new ProductCatalog(user);
	}

	public static Engine getInstance() { return e; }


	// This is the main method which is responsible for the engine
	public void execute() {
		int opt;

		for (; ; ) {
			try {
				opt = initialiseMenu();

				switch (opt) {
					case -1:
						terminateApplication();
					case 1:
						db.printCatalog();
						break;
					case 2:
						scanner.scanProduct();
						break;
					case 3:
						user.userProfileMenu();
						break;
					case 4:
						user.userCartMenu();
						break;
					default:

				}
			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}
		}
	}

	private int initialiseMenu() {
		// General menu
		String mainMenu[] = {"Menu:", "View catalog", "Scan product", "Profile", "Cart", "Exit"};
		GeneralHelperFunctions.generateMenu(mainMenu);


		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			case 0:
			case -1:
				terminateApplication();
			default:
				throw new InputMismatchException();
		}
	}

	public static void terminateApplication() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
		System.exit(0);
	}
}


/* TODO: Use the factory method design pattern combined with dependency inversion to
   TODO: add categories to the products and create the correct object dependant on the category
   TODO: and also use it to create different types of accounts (Basic, Premium, Employee, Manager, Owner)

   TODO: See if you can somehow use Abstract Factory to manage different product attributes depending
   TODO: on the category.
*/

// TODO: Store purchase information about the user in the database
// TODO: Add logout and change user option

// GENERAL TODO: Look into Java best practices if it is the best decision to have that much static classes.