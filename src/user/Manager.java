package user;

import core.Engine;
import lib.GeneralHelperFunctions;
import product.ProductCatalog;

public class Manager extends User {

	public Manager(String username) {
		System.out.println("Manager");
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
			default:
				Engine.terminateApplication();
		}

	}

	private boolean	_addProductToTheCatalog() {
		String newProductName = _createNewProduct();

		if (newProductName != null) {
			if (ProductCatalog.addProductToTheCatalog(db.getProductFromName(newProductName)))
				return true;
		}

		return false;
	}

	private String _createNewProduct() {
		try {
			System.out.print("Name of the product: ");
			String name = Engine.inputScanner.next();

			System.out.print("\nPrice: ");
			double price = Engine.inputScanner.nextDouble();
			//Engine.inputScanner.next();

			System.out.print("\nQuantity: ");
			int quantity = Engine.inputScanner.nextInt();
			//Engine.inputScanner.next();

			if (db.addProductToTheDatabase(name, price, quantity)) {
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
