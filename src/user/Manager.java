package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import product.ProductCatalog;

public class Manager extends User {

	public Manager(String username) {
		setObjectVariables(username, "Manager");
	}

	// TODO: This could be well expanded
	public void initialiseMainMenu() {
		String mainMenu[] = {"Menu: ", "View catalog", "Add product to the catalog", "Profile", "System settings", "Exit"};
		GeneralHelperFunctions.generateMenu(mainMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				ProductCatalog.printCatalog();
				break;
			case 2:
				_addProductToTheCatalog();
				break;
			case 3:
				userProfileMenu();
				break;
			case 0:
				Engine.terminateApplication();
				break;
			default:
				return;
		}
	}

	private boolean	_addProductToTheCatalog() {
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

	public void userProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 2);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(this);
				break;
			case 2:
				UserOperations.editProfile(this);
				break;
			case -1:
				System.out.println("Incorrect input! Please try again.");
				userProfileMenu();
				break;
			case 0:
				return;
			default:
		}

		userProfileMenu();
	}

}
