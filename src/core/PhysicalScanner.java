package core;

import lib.GeneralHelperFunctions;
import product.Product;
import product.ProductCatalog;

import java.util.InputMismatchException;
import java.util.Scanner;

/*
	A class representing a physical scanner. The phone will be used as
	such when implemented on android and the camera will be used to
	scan various barcodes.
 */
// This class implements the Singleton Pattern
// TODO This could implement the observer pattern so that the scanner constantly waits for the id to be passed
public final class PhysicalScanner {

	/* ======== Class Members ======== */
	private static PhysicalScanner ps = new PhysicalScanner();

	// Private constructor to encapsulate the object itself
	private PhysicalScanner() {	}


	/* ======== Public Methods ======== */
	public static PhysicalScanner getInstance() {
		return ps;
	}

	public Product scanProduct() {
		Product p = null;

		System.out.println("Type in the product ID: ");
		long prodID = GeneralHelperFunctions.inputIntegerOption(123_456_00, 123_999_99);

		if (prodID != -1) {
			if (ProductCatalog.productAvailable(prodID)) {
				p = ProductCatalog.getProduct(prodID);
			} else {
				System.out.println("Sorry, this product is currently unavailable or there is no such product with this id.");
			}
		}
		else
			System.out.println("Please provide an ID in the correct format! (123_456_xx)\n");

		return p;
	}
}
