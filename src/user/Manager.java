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

			if (MySQLAccess.getMySQLObject().addProductToTheDatabase(name, price, quantity)) {
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
