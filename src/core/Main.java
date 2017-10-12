package core;
import data.DataFetcher;
import lib.GeneralHelperFunctions;
import user.*;
import product.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

	public static void main(String[] args) {

		// These objects should be created inside the engine class
		//Engine e = Engine.getInstance();
		//e.execute();

		DataFetcher.callMySQL();
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
		System.out.println("\nMenu:");
		System.out.println("\t1. View catalog");
		System.out.println("\t2. Scan product");
		System.out.println("\t3. Profile");
		System.out.println("\t4. Cart");
		System.out.println("\n\t0. Exit");


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


//TODO: create a method which terminates the engine and the program
// TODO: create a method that takes strings as parameters and outputs a menu

// BIG CHANGE TODO: Refactor the code so that all of the logic is inside the Engine class

// GENERAL TODO: Look into Java best practices if it is the best decision to have that much static classes.