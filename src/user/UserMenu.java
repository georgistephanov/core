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
			String userMenu[] = {"Menu:", "Catalog >", "Profile >", "Cart >", "Exit"};
			GeneralHelperFunctions.generateMenu(userMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					_basicUserCatalogMenu();
					break;
				case 2:
					_userProfileMenu();
					break;
				case 3:
					_cartMenu();
					break;
				case 0:
					Engine.getInstance().stopRunning();
					break;
				default:
					throw new InputMismatchException();
			}
		}
		else if (manager != null) {
			String managerMenu[] = {"Menu:", "Catalog >", "Cart >", "Profile >", "Manager menu >", "Exit"};
			GeneralHelperFunctions.generateMenu(managerMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					_managerCatalogMenu();
					break;
				case 2:
					_cartMenu();
					break;
				case 3:
					_userProfileMenu();
					break;
				case 4:
					_managerMenu();
					break;
				case 0:
					Engine.getInstance().stopRunning();
					break;
				default:
			}
		}
		else if (admin != null) {
			String adminMenu[] = {"Admin menu:", "User settings >", "System settings >", "Profile >", "Exit"};
			GeneralHelperFunctions.generateMenu(adminMenu);

			int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
			switch (opt) {
				case 1:
					_adminUserSettings();
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
	private void _userProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile", "Edit profile >", "Card >", "Orders >", "9Logout", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				if (basicUser != null)
					System.out.println(basicUser.toString());
				else if (manager != null)
					System.out.println(manager.toString());
				else
					System.out.println(activeUser.toString());

				break;
			case 2:
				_editProfile();
				break;
			case 3:
				_cardMenu();
				break;
			case 4:
				_userOrdersMenu();
				break;
			case 9:
				activeUser.logout();
				return;
			case 0:
				return;
			default:
		}

		_userProfileMenu();
	}
	private void _adminProfileMenu() {
		String adminProfileMenu[] = {"Profile:", "View profile info", "Edit profile >", "9Logout", "Back"};
		GeneralHelperFunctions.generateMenu(adminProfileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);
		switch (opt) {
			case 1:
				System.out.println(admin.toString());
				break;
			case 2:
				_editProfile();
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
	private void _cartMenu() {
		String cartMenu[] = {"Cart:", "View items", "Checkout", "Cancel line", "Back"};
		GeneralHelperFunctions.generateMenu(cartMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch(opt) {
			case 1:
				activeUser.displayCartItems();
				break;
			case 2:
				if (activeUser.checkout()) {
					return;
				}
				break;
			case 3:
				activeUser.removeItemsFromCart();
				break;
			case 0:
				return;
			default:
				break;
		}

		_cartMenu();
	}


	/* ========== Card Menu ========== */
	private void _cardMenu() {
		String cardMenu[] = {"Card:", "Add new card", "Print card information", "Back"};
		GeneralHelperFunctions.generateMenu(cardMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				UserOperations.addNewCard(activeUser);
				break;
			case 2:
				UserOperations.printCardInformation(activeUser);
				break;
			case 0:
				return;
			default:
		}

		_cardMenu();
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
	private void _userOrdersMenu() {
		String ordersMenu[] = {"Orders:", "Previous orders", "See specific order", "Back"};
		GeneralHelperFunctions.generateMenu(ordersMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				UserOperations.printPreviousOrders(activeUser);
				break;
			case 2:
				UserOperations.printFullPreviousOrder();
				break;
			case 0:
				return;
			default:
				break;
		}

		_userOrdersMenu();
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
	private void _managerCatalogMenu() {
		String menu[] = {"Catalog:", "View catalog", "Scan product", "Add product to the catalog", "Remove product from the catalog", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0,9);
		switch (opt) {
			case 1:
				manager.printCatalog();
				break;
			case 2:
				Product p = PhysicalScanner.getInstance().scanProduct();
				if (p != null) {
					manager.getCart().addToCart(p);
				}
				break;
			case 3:
				manager.addProductToTheCatalog();
				break;
			case 4:
				manager.removeProductFromCatalog();
				break;
			case 0:
				return;
			default:
				break;
		}

		_managerCatalogMenu();
	}


	private void _managerMenu() {
		String menu[] = {"Manager menu", "View previous orders", "Make user premium", "System information", "Back"};
		GeneralHelperFunctions.generateMenu(menu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				UserOperations.printPreviousOrders();
				break;
			case 2:
				UserOperations.makeUserPremium();
				break;
			case 3:
				UserOperations.printSystemInformation();
				break;
			case 0:
				return;
			default:
				break;
		}

		_managerMenu();
	}

	/* ========== Admin specific ========== */
	private void _adminUserSettings() {
		String menu[] = {"Edit users:", "View user information", "Give user privileges", "View previous user sessions", "Delete user", "Back"};
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
				//UserOperations.adminViewUserSessions();
				break;
			case 4:
				UserOperations.adminDeleteUser();
				break;
			case 0:
				return;
			default:

		}

		_adminUserSettings();
	}
}
