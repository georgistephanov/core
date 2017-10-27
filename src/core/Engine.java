package core;

import product.ProductCatalog;
import user.User;
import user.UserFactory;

import java.util.InputMismatchException;
import java.util.Scanner;

//TODO: There are major problems with the scanners and the input in general. Fix them!

// Engine class which implements the Singleton pattern
public class Engine {
	private static Engine e = new Engine();
	public boolean running;

	private User user;
	public static Scanner inputScanner;

	private ProductCatalog catalog = new ProductCatalog();

	private Engine() {

		running = true;
		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");

		inputScanner = new Scanner(System.in);

		user = _promptLogin();
	}


	/* ====== PUBLIC METHODS ====== */
	public static Engine getInstance() { return e; }

	// This is the main method which is responsible for the engine
	public void execute() {
		int opt;

		// Infinite loop which represents the main menu
		for (; ; ) {
			try {
				if (user.isAuthorised())
					user.initialiseMainMenu();

			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}
		}
	}

	public static void terminateApplication() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
		System.exit(0);
	}


	/* ====== PRIVATE METHODS ====== */
	private User _promptLogin() {
		return UserFactory.createUserObject();
	}
}