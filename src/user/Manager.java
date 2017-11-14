package user;

import core.Engine;
import data.Database;
import data.ProductDatabase;
import product.ProductCatalog;

public class Manager extends User {
	private ProductDatabase productDatabase; // This class uses extensively the ProductDatabase class

	/* ========= CONSTRUCTOR ========= */
	Manager(String username) {
		setObjectVariables(username, User.AccountType.MANAGER);
		productDatabase = new ProductDatabase();
	}

	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}
	@Override public String toString() {
		return super.toString() + "\n\nDiscount:\t" + getCheckoutDiscountPercentage() + "%";
	}

	/* ========= USER RELATED METHODS ========= */
	void makeUserPremium() {
		System.out.println("Enter user id: ");
		int id = Engine.getInputScanner().nextInt();
		Engine.getInputScanner().nextLine();

		if (id != 0) {
			if (id > 100_123_00)
				userDatabase.makeUserPremium(id - 100_123_00);
			else
				userDatabase.makeUserPremium(id);
		}
	}

	/* ========== PRODUCT/CATALOG RELATED ========== */
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
		int id = Engine.getInputScanner().nextInt();
		Engine.getInputScanner().nextLine();

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
			String name = Engine.getInputScanner().nextLine();

			System.out.print("\nPrice: ");
			double price = Engine.getInputScanner().nextDouble();
			Engine.flushInputScanner();

			System.out.print("\nQuantity: ");
			int quantity = Engine.getInputScanner().nextInt();
			Engine.flushInputScanner();

			if (productDatabase.addProduct(name, price, quantity)) {
				System.out.println("Product successfully added to the userDatabase.");
				return name;
			} else {
				System.out.println("Something happened while adding the product to the userDatabase. Please try again!");
			}
		}
		catch (Exception e) {
			System.out.println("(Manager: addProductToTheCatalog) " + e.toString());
		}

		return null;
	}

	/* ========= ORDER RELATED METHODS ========= */
	void printPreviousOrders() {
		userDatabase.printPreviousOrders();
	}
}
