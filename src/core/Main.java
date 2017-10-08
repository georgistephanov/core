package core;
import lib.GeneralHelperFunctions;
import sun.java2d.loops.FillRect;
import user.*;
import product.*;

import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
		Engine e = Engine.getInstance();

		BasicUser b1 = new BasicUser("Georgi", "123");
		PhysicalScanner physicalScanner = PhysicalScanner.initialise();
		ProductCatalog catalog = new ProductCatalog(b1);

		int opt; // Variable controlling the execution of the engine
		do {
			opt = e.initialiseMenu();

			switch(opt) {
				case 0:
					break;
				case 1:
					catalog.printCatalog();
					break;
				case 2:
					physicalScanner.scanProduct();
					break;
				case 3:
					// TODO Make a function taking care of this option (3. Profile)
					break;
			}

		} while (opt != -1);
    }
}

// Engine class which implements the Singleton pattern
class Engine {
	private static Engine e = new Engine();
	public boolean running;

	private Engine() {
		running = true;

		System.out.println("CORE Control Centre running...\n");
		System.out.println("Welcome to CORE Control Centre!\n");
	}

	public static Engine getInstance() {
		return e;
	}

	public int initialiseMenu() {
		// General menu
		System.out.println("\nMenu:");
		System.out.println("\t1. View catalog");
		System.out.println("\t2. Scan product");
		System.out.println("\t3. Profile");
		System.out.println("\n4. Cart");
		System.out.println("\t0.Exit");

		Scanner s = new Scanner(System.in);

		try {
			int opt = s.nextInt();
			switch (opt) {
				case 1:
					return 1;
				case 2:
					return 2;
				case 3:
					return 3;
				case 0:
					printExitMessage();
					break;
				default:
					throw new InputMismatchException();
			}
		}
		catch (InputMismatchException e) {
			System.out.println("Please provide a correct input.\nWant to continue? (y/n)");

			if (GeneralHelperFunctions.askForDecision()) {
				return 0;
			}

			printExitMessage();
		}

		return -1;
	}

	private void printExitMessage() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\n\tExiting...\n\n");
	}
}

// These should go inside the cart class (Menu option 4. Cart)
// TODO: Generate receipt
// TODO: Print before checkout [ Qty | Item | Price ]
// TODO: Process Order as a process from the Checkout
// TODO: Cancel line


// GENERAL TODO: Look into Java best practices if it is the best decision to have that much static classes.