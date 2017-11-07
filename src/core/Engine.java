package core;

import lib.GeneralHelperFunctions;
import lib.SystemDiagnostics;
import product.ProductCatalog;
import user.User;
import java.util.InputMismatchException;
import java.util.Scanner;


// Engine class which implements the Singleton pattern
public final class Engine {
	private final static Engine e = new Engine();


	private User user;
	private static Scanner inputScanner;
	private boolean running;


	/* ====== CONSTRUCTOR ====== */
	private Engine() {
		inputScanner = new Scanner(System.in);
		running = true;
		user = promptUserLogin();

		// TODO: Try to decouple this and initialise it in its own class
		ProductCatalog.initialiseCatalog();

		SystemDiagnostics.initialise();
	}

	private User promptUserLogin() {
		User newUser = User.createUserInstance();

		System.out.println("\n========== Welcome to CORE Control Centre! ==========\n");
		System.out.println("\tHello, " + newUser.getFirstName());

		return newUser;
	}


	/* ====== PUBLIC METHODS ====== */
	public static Engine getInstance() { return e; }
	public static Scanner getInputScanner() { return inputScanner; }

	// This is the main method which is responsible for the engine
	void execute() {
		SystemDiagnostics.getInstance().runStartupTest();

		// Perform system diagnostics test every 5 seconds
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(SystemDiagnostics.getInstance(), 0, 5000);

		// Infinite loop which represents the main menu
		while (running) {
			try {
				if (user.isAuthorised()) {
					user.initialiseMainMenu();
				} else {
					System.out.println("\n\nWould you like to log in with different account?");

					if (GeneralHelperFunctions.askForDecision()) {
						promptUserLogin();
					} else {
						printExitMessage();
						terminateApplication();
					}
				}

			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}
		}

		printExitMessage();
		terminateApplication();
	}

	public boolean isUserAuthorised() { return this.user.isAuthorised(); }

	public boolean isRunning() { return running; }

	public static void terminateApplication() {
		System.exit(0);
	}

	/* ====== PRIVATE METHODS ====== */
	private void printExitMessage() {
		System.out.println("\n\n\tThank you for using CORE Control Centre!");
		System.out.print("\tExiting...\n\n");
	}
}