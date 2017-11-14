package product;

import java.util.ArrayList;
import data.*;

public final class ProductCatalog {
	private static ArrayList<Product> _products = new ArrayList<>();

	/* ============== PUBLIC METHODS ============== */
	public static void initialiseCatalog() {
		_products = new ProductDatabase().getProductsFromDatabase();
	}

	public static boolean addProductToTheCatalog(Product p) {
		if (p != null) {
			_products.add(p);
			return true;
		}

		return false;
	}

	public static void printCatalog() {
		if (!_products.isEmpty()) {
			for(Product p: _products) {
				System.out.println(p.toString());
			}
		}
	}

	public static boolean productAvailable(long id) {
		return _isProductAvailable(id);
	}

	public static Product getProduct(long id) {
		Product p = null;
		for (Product pr : _products)
			if (pr.getID() == id) {
				p = pr;
				break;
			}

		return p;
	}

	public static boolean isAvailable() { return !_products.isEmpty(); }


	/* ============== HELPER METHODS ============== */
	private static boolean _isProductAvailable(long id) {
		for(Product p : _products) {
			if (p.getID() == id)
				return true;
		}

		return false;
	}
}
