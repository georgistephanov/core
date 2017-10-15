package product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import data.*;
import user.BasicUser;

// THIS CLASS SIMULATES REAL DATABASE AT THE MOMENT
// WILL NOT EXIST WHEN A DB IS PRESENT
public class ProductCatalog {
	private static BasicUser associatedUser;

	public static ArrayList<Product> catalog = new ArrayList<>();


	/* ============== PUBLIC METHODS ============== */

	// Generates a catalog from the products in the database
	public ProductCatalog(BasicUser user) {
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
		if (_isProductAvailable(id)) {
			Product p = ProductCatalog.getProduct(id);
			associatedUser.addToCart(p);
			return true;
		}
		else {
			System.out.println("There is no such product in our database.\n");
			return false;
		}
	}


	/* ============== PRIVATE METHODS ============== */

	private static boolean _isProductAvailable(long id) {
		for(Product p : catalog) {
			if (p.productID == id)
				return true;
		}

		return false;
	}

	private static Product getProduct(long id) {
		Product p = null;
		for (Product pr : catalog)
			if (pr.productID == id) {
				p = pr;
				break;
			}

		return p;
	}

}
