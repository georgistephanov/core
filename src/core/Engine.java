package core;

import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import product.ProductCatalog;
import user.User;
import user.UserFactory;

import java.util.InputMismatchException;
import java.util.Scanner;

// Engine class which implements the Singleton pattern
public class Engine {
	private static Engine e = new Engine();
	public boolean running;

	private User user;
	private PhysicalScanner scanner;
	private static ProductCatalog db;

	public static Scanner inputScanner;

	private Engine() {
		running = true;

		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");

		inputScanner = new Scanner(System.in);

		user = promptLogin();

		scanner = PhysicalScanner.initialise();
		db = new ProductCatalog(user);
	}

	public static Engine getInstance() { return e; }

	public static void printCatalog() {
		db.printCatalog();
	}

	// This is the main method which is responsible for the engine
	// TODO: This should be reimplemented
	public void execute() {
		int opt;

		for (; ; ) {
			try {
				opt = user.initialiseMainMenu();

				if (user.authorised) {
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
				}

			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}
		}
	}

	private User promptLogin() {
		return UserFactory.createUserObject();
	}

	public static void terminateApplication() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
		System.exit(0);
	}
}