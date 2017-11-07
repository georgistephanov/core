package user;

import core.Engine;
import core.PhysicalScanner;
import lib.GeneralHelperFunctions;
import product.Product;
import java.util.InputMismatchException;


final class UserMenu {
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

	void initialiseMainMenu() {
		if (basicUser != null) {
			// General menu
			String mainMenu[] = {"Menu:", "Catalog >", "Profile >", "Cart >", "Exit"};
			GeneralHelperFunctions.generateMenu(mainMenu);


			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					_basicUserCatalogMenu();
					break;
				case 2:
					_basicUserProfileMenu();
					break;
				case 3:
					_basicUserCartMenu();
					break;
				case 0:
					Engine.terminateApplication();
				case -1:
					break;
				default:
					throw new InputMismatchException();
			}
		}
		else if (manager != null) {
			String mainMenu[] = {"Menu: ", "Catalog >", "Profile >", "System settings >", "Exit"};
			GeneralHelperFunctions.generateMenu(mainMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					_managerCatalogMenu();
					break;
				case 2:
					_managerProfileMenu();
					break;
				case 3:
					_managerSystemSettingsMenu();
					break;
				case 0:
					Engine.terminateApplication();
				default:
			}
		}
		else if (admin != null) {

		}
	}



	/* ========== Profile Menu ========== */
	private void _basicUserProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile", "Edit profile >", "Card >", "Orders >", "9Logout", "Back"};

		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 4);

		switch (opt) {
			case 1:
				System.out.println(basicUser.toString());
				break;
			case 2:
				_basicUserEditProfileMenu();
				break;
			case 3:
				_basicUserCardMenu();
				break;
			case 4:
				_basicUserOrdersMenu();
				break;
			case 9:
				// TODO: This doesn't make the right call
				basicUser.logout();
				return;
			case 0:
				return;
			default:
		}

		_basicUserProfileMenu();
	}
	private void _managerProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile >", "Make user premium", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				System.out.println(manager.toString());
				break;
			case 2:
				_managerEditProfileMenu();
				break;
			case 3:
				UserOperations.makeUserPremium();
			case -1:
				System.out.println("Incorrect input! Please try again.");
				_managerProfileMenu();
				break;
			case 0:
				return;
			default:
		}

		_managerProfileMenu();
	}


	/* ========== Cart Menu ========== */
	private void _basicUserCartMenu() {
		String cartMenu[] = {"Cart:", "View items", "Checkout", "Cancel line", "Back"};
		GeneralHelperFunctions.generateMenu(cartMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				basicUser.displayCartItems();
				break;
			case 2:
				if (basicUser.checkout())
					return;

				break;
			case 3:
				basicUser.removeItemsFromCart();
				break;
			case 0:
				return;
			default:
				break;
		}

		_basicUserCartMenu();
	}


	/* ========== Card Menu ========== */
	private void _basicUserCardMenu() {
		String cardMenu[] = {"Card:", "Add new card", "Print card information", "Back"};
		GeneralHelperFunctions.generateMenu(cardMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				UserOperations.addNewCard(this.basicUser);
				break;
			case 2:
				UserOperations.printCardInformation(this.basicUser);
				break;
			case 0:
				return;
			default:
		}

		_basicUserCardMenu();
	}


	/* ========== Edit profile Menu ========== */
	private void _basicUserEditProfileMenu() {
		String menu[] = {"Which field would you like to edit?", "First name", "Last name", "Change password", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);
		switch (opt) {
			case 1:
				UserOperations.changeFirstName(this.basicUser);
				break;
			case 2:
				UserOperations.changeLastName(this.basicUser);
				break;
			case 3:
				UserOperations.changePassword(this.basicUser);
				break;
			case 0:
				return;
			default:
		}

		_basicUserEditProfileMenu();
	}
	private void _managerEditProfileMenu() {
		String menu[] = {"Which field would you like to edit?", "First name", "Last name", "Change password", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);
		switch (opt) {
			case 1:
				UserOperations.changeFirstName(this.manager);
				break;
			case 2:
				UserOperations.changeLastName(this.manager);
				break;
			case 3:
				UserOperations.changePassword(this.manager);
				break;
			case 0:
				return;
			default:
		}

		_managerEditProfileMenu();
	}

	/* ========== Orders Menu ========== */
	private void _basicUserOrdersMenu() {
		String ordersMenu[] = {"Orders:", "Previous orders", "See specific order", "Back"};
		GeneralHelperFunctions.generateMenu(ordersMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				UserOperations.printPreviousOrders(this.basicUser);
				break;
			case 2:
				UserOperations.printFullPreviousOrder();
				break;
			case 0:
				return;
			default:
				break;
		}

		_basicUserOrdersMenu();
	}

	/* ========== Catalog Menu ========== */
	private void _managerCatalogMenu() {
		String menu[] = {"Catalog:", "View catalog", "Add product", "Remove product", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0,9);
		switch (opt) {
			case 1:
				manager.printCatalog();
				break;
			case 2:
				manager.addProductToTheCatalog();
				break;
			case 3:
				manager.removeProductFromCatalog();
				break;
			case 0:
				return;
			default:
				break;
		}

		_managerCatalogMenu();
	}


	/* ========== Catalog Menu ========== */
	private void _basicUserCatalogMenu() {
		String menu[] = {"Catalog:", "View catalog", "Search product by name", "Scan product", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				basicUser.printCatalog();
				break;
			case 2:
				UserOperations.searchProductByName();
				break;
			case 3:
				Product p = PhysicalScanner.getInstance().scanProduct();
				if ( p != null) {
					basicUser.getCart().addToCart(p);
				}
				break;
			case 0:
				return;
			default:
				break;
		}

		_basicUserCatalogMenu();
	}

	private void _managerSystemSettingsMenu() {
		String menu[] = {"System settings", "System information", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				UserOperations.printSystemInformation();
				break;
			case 0:
				return;
			default:
				break;
		}

		_managerSystemSettingsMenu();
	}
}
