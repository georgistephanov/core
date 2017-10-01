package product;

import java.io.IOException;
import java.util.ArrayList;
import data.*;

public class ProductCatalog {
	public static ArrayList<Product> catalog = new ArrayList<>();

	public ProductCatalog() {
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

	public static Product productAvailable(long id) {
		for(Product p : catalog) {
			if (p.productID == id)
				return p;
		}

		return null;
	}

}
