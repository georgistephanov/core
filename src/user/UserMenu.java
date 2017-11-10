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
	private User activeUser;

	private UserMenu(User user) {
		activeUser = user;
	}

	UserMenu(BasicUser basicUser) {
		this((User) basicUser);
		this.basicUser = basicUser;
	}

	UserMenu(Manager manager) {
		this((User) manager);
		this.manager = manager;
	}

	UserMenu(Admin admin) {
		this((User) admin);
		this.admin = admin;
	}

	void initialiseMainMenu() {
		if (basicUser != null) {
			// General menu
			String userMenu[] = {"Menu:", "Catalog >", "Profile >", "Cart >", "Exit"};
			GeneralHelperFunctions.generateMenu(userMenu);


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
					Engine.getInstance().stopRunning();
					break;
				default:
					throw new InputMismatchException();
			}
		}
		else if (manager != null) {
			String managerMenu[] = {"Menu:", "Catalog >", "Profile >", "System settings >", "Exit"};
			GeneralHelperFunctions.generateMenu(managerMenu);

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
					Engine.getInstance().stopRunning();
					break;
				default:
			}
		}
		else if (admin != null) {
			String adminMenu[] = {"Admin menu:", "User settings", "System settings >", "Profile >", "Exit"};
			GeneralHelperFunctions.generateMenu(adminMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					//_adminUserSettingsMenu();
					break;
				case 2:
					//_adminSystemSettingsMenu();
					break;
				case 3:
					_adminProfileMenu();
					break;
				case 0:
					Engine.getInstance().stopRunning();
					break;
				default:

			}
		}
	}



	/* ========== Profile Menu ========== */
	private void _basicUserProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile", "Edit profile >", "Card >", "Orders >", "9Logout", "Back"};

		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				System.out.println(basicUser.toString());
				break;
			case 2:
				_editProfile();
				break;
			case 3:
				_basicUserCardMenu();
				break;
			case 4:
				_basicUserOrdersMenu();
				break;
			case 9:
				basicUser.logout();
				return;
			case 0:
				return;
			default:
		}

		_basicUserProfileMenu();
	}
	private void _managerProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile >", "Make user premium", "9Logout", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				System.out.println(manager.toString());
				break;
			case 2:
				_editProfile();
				break;
			case 3:
				UserOperations.makeUserPremium();
			case 9:
				manager.logout();
				return;
			case 0:
				return;
			default:
		}

		_managerProfileMenu();
	}
	private void _adminProfileMenu() {
		String adminProfileMenu[] = {"Profile:", "View profile info", "Edit profile >", "Edit users >", "9Logout", "Back"};
		GeneralHelperFunctions.generateMenu(adminProfileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				System.out.println(admin.toString());
				break;
			case 2:
				_editProfile();
				break;
			case 3:
				_adminEditUsers();
				break;
			case 9:
				admin.logout();
				return;
			case 0:
				return;
			default:

		}

		_adminProfileMenu();
	}

	// TODO: Add logic to detect previous user sessions and time being logged in

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
	private void _editProfile() {
		String menu[] = {"Which field would you like to edit?", "First name", "Last name", "Change password", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);
		switch (opt) {
			case 1:
				UserOperations.changeFirstName(activeUser);
				break;
			case 2:
				UserOperations.changeLastName(activeUser);
				break;
			case 3:
				UserOperations.changePassword(activeUser);
				break;
			case 0:
				return;
			default:
		}

		_editProfile();
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

	/* ========== Admin specific ========== */
	private void _adminEditUsers() {
		String menu[] = {"Edit users:", "View user information", "Give user privileges", "Delete user", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				UserOperations.adminViewUserInformation();
				break;
			case 2:
				UserOperations.adminGiveUserPrivileges();
				break;
			case 3:
				//UserOperations.adminDeleteUser();
				break;
			case 0:
				return;
			default:

		}

		_adminEditUsers();
	}
}
