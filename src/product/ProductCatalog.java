package product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import data.*;
import user.BasicUser;
import user.User;

// THIS CLASS SIMULATES REAL DATABASE AT THE MOMENT
// WILL NOT EXIST WHEN A DB IS PRESENT
public class ProductCatalog {
	private static User associatedUser;

	public static ArrayList<Product> catalog = new ArrayList<>();


	/* ============== PUBLIC METHODS ============== */

	// Generates a catalog from the products in the database
	public ProductCatalog(User user) {
		try {
			MySQLAccess db = MySQLAccess.getMySQLObject();

			catalog = db.getProductsFromDatabase();

			associatedUser = user;
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void printCatalog() {
		if (!catalog.isEmpty()) {
			for(Product p: catalog) {
				p.printProductInfo();
			}
		}
	}

	public static boolean productAvailable(long id) {
		return _isProductAvailable(id);
	}


	/* ============== PRIVATE METHODS ============== */

	private static boolean _isProductAvailable(long id) {
		for(Product p : catalog) {
			if (p.productID == id)
				return true;
		}

		return false;
	}

	public static Product getProduct(long id) {
		Product p = null;
		for (Product pr : catalog)
			if (pr.productID == id) {
				p = pr;
				break;
			}

		return p;
	}

}
