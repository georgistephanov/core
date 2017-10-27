package user;

import core.Engine;
import core.PhysicalScanner;
import lib.GeneralHelperFunctions;
import product.Product;
import product.ProductCatalog;

import java.util.InputMismatchException;

public final class UserMenu {
	private BasicUser basicUser;
	private Manager manager;
	private Admin admin;

	UserMenu(BasicUser user) {
		this.basicUser = user;
	}

	UserMenu(Manager manager) {
		this.manager = manager;
	}

	UserMenu(Admin admin) {
		this.admin = admin;
	}

	public void initialiseMainMenu() {
		if (basicUser != null) {
			// General menu
			String mainMenu[] = {"Menu:", "View catalog", "Scan product", "Profile", "Cart", "Exit"};
			GeneralHelperFunctions.generateMenu(mainMenu);


			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					ProductCatalog.printCatalog();
					break;
				case 2:
					Product p = PhysicalScanner.getInstance().scanProduct();
					if ( p != null) {
						basicUser.getCart().addToCart(p);
					}
					break;
				case 3:
					initialiseBasicUserProfileMenu();
					break;
				case 4:
					initialiseBasicUserCartMenu();
					break;
				case 0:
					Engine.terminateApplication();
				case -1:
					return;
				default:
					throw new InputMismatchException();
			}
		}
		else if (manager != null) {
			String mainMenu[] = {"Menu: ", "View catalog", "Add product to the catalog", "Remove product from the catalog", "Profile", "System settings", "Exit"};
			GeneralHelperFunctions.generateMenu(mainMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					ProductCatalog.printCatalog();
					break;
				case 2:
					manager.addProductToTheCatalog();
					break;
				case 3:
					manager.removeProductFromCatalog();
					break;
				case 4:
					initialiseManagerProfileMenu();
					break;
				case 0:
					Engine.terminateApplication();
					break;
				default:
					return;
			}
		}
		else if (admin != null) {

		}
	}


	// TODO: Add options to add and remove cards from the account
	private void initialiseBasicUserProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile", "Change password", "Add card", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 4);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(basicUser);
				break;
			case 2:
				UserOperations.editProfile(basicUser);
				break;
			case 3:
				UserOperations.changePassword(basicUser);
				break;
			case 4:
				UserOperations.addNewCard(basicUser);
				break;
			case -1:
				System.out.println("Incorrect input! Please try again.");
				initialiseBasicUserProfileMenu();
			case 0:
				initialiseMainMenu();
			default:
				break;
		}

		initialiseBasicUserProfileMenu();
	}

	private void initialiseBasicUserCartMenu() {
		String cartMenu[] = {"Cart:", "View items", "Checkout", "Cancel line", "Back"};
		GeneralHelperFunctions.generateMenu(cartMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				basicUser.displayCartItems();
				break;
			case 2:
				if (basicUser.beginCheckoutProcess()) {
					initialiseMainMenu();
				}
				break;
			case 3:
				basicUser.removeItemsFromCart();
				break;
			case 0:
				initialiseMainMenu();
			default:
				break;
		}

		initialiseBasicUserCartMenu();
	}


	private void initialiseManagerProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile", "Make user premium", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(manager);
				break;
			case 2:
				UserOperations.editProfile(manager);
				break;
			case 3:
				UserOperations.makeUserPremium(manager);
			case -1:
				System.out.println("Incorrect input! Please try again.");
				initialiseManagerProfileMenu();
				break;
			case 0:
				return;
			default:
		}

		initialiseManagerProfileMenu();
	}
}
