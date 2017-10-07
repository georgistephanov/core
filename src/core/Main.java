package core;
import user.*;
import product.*;

import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
		BasicUser b1 = new BasicUser("Georgi", "123");
		ProductCatalog catalog = new ProductCatalog(b1);
		catalog.printCatalog();

        System.out.println();

		PhysicalScanner physicalScanner = PhysicalScanner.initialise();

		try {
			physicalScanner.scanProduct();
		}
		catch (InputMismatchException e) {
			System.out.println(e.toString());
		}
    }
}

// TODO: Make a user menu (infinite loop)
// TODO: Generate receipt
// TODO: Print before checkout [ Qty | Item | Price ]
// TODO: Process Order as a process from the Checkout
// TODO: Cancel line