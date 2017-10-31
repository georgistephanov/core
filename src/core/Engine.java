package core;

import lib.SystemDiagnostics;
import product.ProductCatalog;
import user.User;
import java.util.InputMismatchException;
import java.util.Scanner;


// Engine class which implements the Singleton pattern
public final class Engine {
	private final static Engine e = new Engine();

	private User user;
	public static Scanner inputScanner;
	private boolean running;


	/* ====== CONSTRUCTOR ====== */
	private Engine() {
		inputScanner = new Scanner(System.in);
		running = true;
		user = User.createUserInstance();

		// TODO: Try to decouple this and initialise it in its own class
		ProductCatalog.initialiseCatalog();
	}


	/* ====== PUBLIC METHODS ====== */
	public static Engine getInstance() { return e; }

	// This is the main method which is responsible for the engine
	void execute() {
		System.out.println("CORE Control Centre running...");
		System.out.println("Welcome to CORE Control Centre!\n");

		System.out.println("Hello, " + this.user.getUsername());

		// Infinite loop which represents the main menu
		while (running) {
			SystemDiagnostics.getInstance().testSystem();

			try {
				if (user.isAuthorised())
					user.initialiseMainMenu();

			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}
		}

		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
	}

	public boolean isRunning() { return running; }

	public static void terminateApplication() {
		System.exit(0);
	}

	/* ====== PRIVATE METHODS ====== */

}