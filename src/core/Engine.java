package core;

import data.SystemDatabase;
import lib.GeneralHelperFunctions;
import lib.Logger;
import lib.SystemDiagnostics;
import lib.observer.Observer;
import product.ProductCatalog;
import user.User;

import java.util.ArrayList;
import java.util.Scanner;

public final class Engine implements lib.observer.Subject {
	private final static Engine e = new Engine();
	private ArrayList<Observer> observers;

	private User user;
	private static Scanner inputScanner;
	private boolean running;


	/* ====== CONSTRUCTOR ====== */
	private Engine() {
		observers = new ArrayList<>();
		registerObserver(SystemDiagnostics.getInstance());

		inputScanner = new Scanner(System.in);
		running = true;
		user = promptUserLogin();

		ProductCatalog.initialiseCatalog();

		// Shutdown hook to logout the user when the application is closed
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (SystemDiagnostics.getInstance().isUserAuthorised()) {
				this.user.hardLogout();
			}

			new SystemDatabase().registerTestsPerformed(SystemDiagnostics.getInstance().getTestsPerformed(), SystemDiagnostics.getInstance().getTestsPassed());
		}));
	}

	/* ====== OBSERVER PATTERN METHODS ====== */
	public void registerObserver(Observer o) {
		observers.add(o);
	}
	public void removeObserver(Observer o) {
		if (observers.contains(o)) {
			observers.remove(o);
		}
	}
	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update();
		}
	}


	/* ====== This is the main method which is responsible for the engine ====== */
	void execute() {
		SystemDiagnostics.getInstance().startTimer();
		notifyObservers();

		// Infinite loop which represents the main menu
		while (running) {
			try {
				if (user.isAuthorised()) {
					user.initialiseMainMenu();
				} else {
					System.out.println("\n\nWould you like to log in with different account?");

					if (GeneralHelperFunctions.askForDecision()) {
						this.user = promptUserLogin();
						notifyObservers();
					} else {
						printExitMessage();
						terminateApplication();
					}
				}

			} catch (Exception e) {
				Logger.getInstance().logError(e, "Engine", "execute");
			}
		}

		SystemDiagnostics.getInstance().stopTimer();
		printExitMessage();
		terminateApplication();
	}


	/* ====== GENERAL METHODS ====== */
	public static Engine getInstance() { return e; }
	public static Scanner getInputScanner() { return inputScanner; }
	public static void flushInputScanner() { inputScanner.nextLine(); }
	public void stopRunning() { this.running = false; }


	/* ====== USER RELATED METHODS ====== */
	public void userLoggedOut() {
		// This method is called from the User's method logout() to avoid making it subject as well

		notifyObservers();
	}
	public boolean isUserAuthorised() { return this.user.isAuthorised(); }
	private User promptUserLogin() {
		User newUser = User.createUserInstance();

		GeneralHelperFunctions.printBlockMessage(" Welcome to CORE Control Centre! ");
		System.out.println("\tHello, " + newUser.getFirstName());

		return newUser;
	}


	/* ====== CONTROL-FLOW METHODS ====== */
	public boolean isRunning() { return running; }
	public void terminateApplication() {
		System.exit(0);
	}
	private void printExitMessage() {
		GeneralHelperFunctions.printBlockMessage("Thank you for using CORE Control Centre!", "Exiting...");
	}
}