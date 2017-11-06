package user;

import core.Engine;
import data.Database;
import data.ProductDatabase;
import product.ProductCatalog;

public class Manager extends User {
	// This class uses extensively the ProductDatabase class
	ProductDatabase productDatabase;

	Manager(String username) {
		setObjectVariables(username, User.AccountType.MANAGER);
		productDatabase = new ProductDatabase();
	}

	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}

	void addProductToTheCatalog() {
		String newProductName = _createNewProduct();

		if (newProductName != null) {
			if (ProductCatalog.addProductToTheCatalog(productDatabase.getProductFromName(newProductName)))
				System.out.println("Product successfully added to the catalog.");
		}

		System.out.println("The product has not been added to the catalog. Please try again in a few moments.");
	}

	 void removeProductFromCatalog() {
		System.out.println("Enter product ID");
		int id = Engine.inputScanner.nextInt();
		Engine.inputScanner.nextLine();

		if (id != 0) {

			if (id > 123_456_00)
				productDatabase.removeProduct(id - 123_456_00);
			else
				productDatabase.removeProduct(id);

		} else {
			System.out.println("Invalid ID entered");
		}
	}

	private String _createNewProduct() {
		try {
			System.out.print("Name of the product: ");
			String name = Engine.inputScanner.nextLine();

			System.out.print("\nPrice: ");
			double price = Engine.inputScanner.nextDouble();

			System.out.print("\nQuantity: ");
			int quantity = Engine.inputScanner.nextInt();
			//Engine.inputScanner.next();

			if (productDatabase.addProduct(name, price, quantity)) {
				System.out.println("Product successfully added to the database.");
				return name;
			} else {
				System.out.println("Something happened while adding the product to the database. Please try again!");
			}
		}
		catch (Exception e) {
			System.out.println("(Manager: addProductToTheCatalog) " + e.toString());
		}

		return null;
	}

	public String toString() {
		return super.toString() + "\n\nDiscount:\t" + getCheckoutDiscountPercentage() + "%";
	}
}
