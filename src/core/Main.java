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
					b1.userProfileMenu();
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
		System.out.println("\t4. Cart");
		System.out.println("\n\t0. Exit");


		int opt = GeneralHelperFunctions.inputIntegerOption(1, 4);
		switch (opt) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case -1:
				printExitMessage();
				break;
			default:
				throw new InputMismatchException();
		}

		return -1;
	}

	private void printExitMessage() {
		System.out.println("\n\n\tThank you for using CORE Cashless!");
		System.out.print("\tExiting...\n\n");
	}
}

// These should go inside the cart class (Menu option 4. Cart)
// TODO: Generate receipt
// TODO: Print before checkout [ Qty | Item | Price ]
// TODO: Process Order as a process from the Checkout
// TODO: Cancel line


// TODO: create a method that takes strings as parameters and outputs a menu


// GENERAL TODO: Look into Java best practices if it is the best decision to have that much static classes.