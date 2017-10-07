package product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import data.*;
import user.BasicUser;

public class ProductCatalog {
	private static BasicUser associatedUser;

	public static ArrayList<Product> catalog = new ArrayList<>();

	public ProductCatalog(BasicUser user) {
		// Initialise the product catalog
		// TODO: Refactor this
		try {
			ArrayList<String> fileData;
			fileData = DataFetcher.readFromFile("D:\\Projects\\Java Playground\\CORE Control Centre\\data\\products.txt", 20);

			String productInfo[] = new String[3];

			int i=0;
			for (String line : fileData) {
				if (i < 3) {
					productInfo[i] = line;
					i++;
				}
				else {
					double price = Double.parseDouble(productInfo[1]);
					int quantity = Integer.parseInt(productInfo[2]);

					catalog.add(new Product(productInfo[0], price, quantity));
					i = 0;
				}
			}

			associatedUser = user;
		}
		catch (IOException e) {
			System.out.println("Error reading the products file.");
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

	// PRIVATE METHODS
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
