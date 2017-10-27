package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import product.ProductCatalog;

public class Manager extends User {

	public Manager(String username) {
		setObjectVariables(username, "Manager");
	}

	public void initialiseMainMenu() {
		UserMenu menu = new UserMenu(this);
		menu.initialiseMainMenu();
	}

	protected boolean addProductToTheCatalog() {
		String newProductName = _createNewProduct();

		if (newProductName != null) {
			if (ProductCatalog.addProductToTheCatalog(MySQLAccess.getMySQLObject().getProductFromName(newProductName)))
				return true;
		}

		return false;
	}

	protected boolean removeProductFromCatalog() {
		System.out.println("Enter product ID");
		int id = Engine.inputScanner.nextInt();
		Engine.inputScanner.nextLine();

		if (id != 0) {
			// TODO: fix the parameter remainder hack
			return MySQLAccess.getMySQLObject().removeProduct(id % 100);
		} else {
			System.out.println("Invalid ID entered");
		}

		return false;
	}

	protected void makeUserPremium() {
		System.out.println("Enter user id: ");
		int id = Engine.inputScanner.nextInt();
		Engine.inputScanner.nextLine();

		if (id != 0) {
			// TODO: Fix this parameter hack
			MySQLAccess.getMySQLObject().makeUserPremium(id % 100);
		}
	}

	private String _createNewProduct() {
		try {
			Engine.inputScanner.nextLine();
			System.out.print("Name of the product: ");
			String name = Engine.inputScanner.nextLine();

			System.out.print("\nPrice: ");
			double price = Engine.inputScanner.nextDouble();
			//Engine.inputScanner.next();

			System.out.print("\nQuantity: ");
			int quantity = Engine.inputScanner.nextInt();
			//Engine.inputScanner.next();

			if (MySQLAccess.getMySQLObject().addProduct(name, price, quantity)) {
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
}
