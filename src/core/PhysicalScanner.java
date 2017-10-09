package core;

import lib.GeneralHelperFunctions;
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

	public void scanProduct() {
		System.out.println("Type in the product ID: ");
		long prodID = GeneralHelperFunctions.inputIntegerOption(123_456_00, 123_999_99);

		if (prodID != -1)
			ProductCatalog.productAvailable(prodID);
		else
			System.out.println("Please provide an ID in the correct format! (123_456_xx)\n");

	}
}
