package core;

import user.User;
import java.util.InputMismatchException;
import java.util.Scanner;


// Engine class which implements the Singleton pattern
public final class Engine {
	private final static Engine e = new Engine();
	public boolean running;

	private User user;
	public static Scanner inputScanner;


	/* ====== CONSTRUCTOR ====== */
	private Engine() {
		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");

		inputScanner = new Scanner(System.in);
		running = true;
		user = _promptLogin();
	}


	/* ====== PUBLIC METHODS ====== */
	public final static Engine getInstance() { return e; }

	// This is the main method which is responsible for the engine
	public final void execute() {

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

	public final static void terminateApplication() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
		System.exit(0);
	}


	/* ====== PRIVATE METHODS ====== */
	private final User _promptLogin() {
		return User.createUserInstance();
	}
}