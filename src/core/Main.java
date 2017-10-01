package core;
import user.*;
import product.*;

import java.io.IOException;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {
		BasicUser b1 = new BasicUser("Georgi", "123");
		ProductCatalog catalog = new ProductCatalog();
		catalog.printCatalog();

		try {
			b1.scanProduct();
		}
		catch (InputMismatchException e) {
			System.out.println(e.toString());
		}

        System.out.println();
    }
}

// TODO: Generate receipt
// TODO: Print before checkout [ Qty | Item | Price ]
// TODO: Process Order as a process from the Checkout
// TODO: Cancel line