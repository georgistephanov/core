package product;

import java.util.ArrayList;
import data.*;

// THIS CLASS SIMULATES REAL DATABASE AT THE MOMENT
// WILL NOT EXIST WHEN A DB IS PRESENT
public final class ProductCatalog {
	// An array list which holds all the products
	private static ArrayList<Product> catalog = new ArrayList<>();

	static {
		try {
			MySQLAccess db = MySQLAccess.getMySQLObject();
			catalog = db.getProductsFromDatabase();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	/* ============== PUBLIC METHODS ============== */

	// Generates a catalog from the products in the database
	public ProductCatalog() {
		// TODO: Consider whether to leave this as it is or the class should be redesigned
	}

	public static boolean addProductToTheCatalog(Product p) {
		if (p != null) {
			catalog.add(p);
			return true;
		}

		return false;
	}

	public static void printCatalog() {
		if (!catalog.isEmpty()) {
			for(Product p: catalog) {
				p.printProductInfo();
			}
		}
	}

	public static boolean productAvailable(long id) {
		return _isProductAvailable(id);
	}

	public static Product getProduct(long id) {
		Product p = null;
		for (Product pr : catalog)
			if (pr.getID() == id) {
				p = pr;
				break;
			}

		return p;
	}


	/* ============== PRIVATE METHODS ============== */

	private static boolean _isProductAvailable(long id) {
		for(Product p : catalog) {
			if (p.getID() == id)
				return true;
		}

		return false;
	}
}
