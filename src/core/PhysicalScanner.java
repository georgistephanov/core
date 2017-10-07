package core;

import product.Product;
import product.ProductCatalog;

import java.util.InputMismatchException;
import java.util.Scanner;

// This class implements the Singleton Pattern
// TODO This could implement the observer pattern so that the scanner constantly waits for the id to be passed
public class PhysicalScanner {
	private static PhysicalScanner ps = new PhysicalScanner();

	private PhysicalScanner() {	}

	public static PhysicalScanner initialise() {
		return ps;
	}

	public void scanProduct() throws InputMismatchException {
		Scanner scanner = new Scanner(System.in);
		long prodID;

		System.out.println("Type in the product ID: ");
		prodID = scanner.nextLong();

		ProductCatalog.productAvailable(prodID);
	}
}
