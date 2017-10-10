package core;
import jdk.internal.util.xml.impl.Input;
import lib.GeneralHelperFunctions;
import sun.java2d.loops.FillRect;
import user.*;
import product.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {

    	// These objects should be created inside the engine class
		Engine e = Engine.getInstance(new BasicUser("Georgi", "123"), PhysicalScanner.initialise());

		int opt; // Variable controlling the execution of the engine
		for ( ; ; ) {
			try {
				opt = e.initialiseMenu();

				switch (opt) {
					case -1:
						e.terminateApplication();
					case 1:
						catalog.printCatalog();
						break;
					case 2:
						physicalScanner.scanProduct();
						break;
					case 3:
						b1.userProfileMenu();
						break;
					case 4:
						b1.userCartMenu();
						break;
					default:

				}
			} catch (InputMismatchException exc) {
				System.out.println("\nPlease provide a correct input!");
			}

		}
    }
}

// Engine class which implements the Singleton pattern
class Engine {
	private static Engine e;
	public boolean running;

	private BasicUser user;
	private PhysicalScanner scanner;
	private ProductCatalog catalog;

	private Engine(BasicUser user, PhysicalScanner scanner) {
		running = true;

		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");

		this.user = user;
		this.scanner = scanner;
		catalog = new ProductCatalog(this.user)
	}

	public static Engine getInstance(BasicUser u, PhysicalScanner ps,) {
		if (e == null) {
			e = new Engine(u, ps);
		}

		return e;
	}

	public int initialiseMenu() {
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