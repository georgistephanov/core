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
			String mainMenu[] = {"Menu:", "View catalog", "Scan product", "Profile >", "Cart >", "Exit"};
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
					_basicUserProfileMenu();
					break;
				case 4:
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
			String mainMenu[] = {"Menu: ", "View catalog", "Add product to the catalog", "Remove product from the catalog", "Profile >", "System settings >", "Exit"};
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
					_managerProfileMenu();
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



	/* ========== Profile Menu ========== */
	private void _basicUserProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile", "Edit profile >", "Card >", "Orders >", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 4);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(basicUser);
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
			case 0:
				return;
			default:
				break;
		}

		_basicUserProfileMenu();
	}
	private void _managerProfileMenu() {
		String profileMenu[] = {"Profile:", "View profile info", "Edit profile >", "Make user premium", "Back"};
		GeneralHelperFunctions.generateMenu(profileMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				UserOperations.printProfileInfo(this.manager);
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
				if (basicUser.beginCheckoutProcess())
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
		String cardMenu[] = {"Card:", "Add new card", "Check balance", "Back"};
		GeneralHelperFunctions.generateMenu(cardMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 9);

		switch (opt) {
			case 1:
				UserOperations.addNewCard(this.basicUser);
				break;
			case 2:
				UserOperations.printCardBalance(this.basicUser);
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
		String ordersMenu[] = {"Orders:", "See previous orders", "Back"};
		GeneralHelperFunctions.generateMenu(ordersMenu);

		int opt = GeneralHelperFunctions.inputIntegerOption(0, 3);

		switch (opt) {
			case 1:
				UserOperations.printPreviousOrders(this.basicUser);
				break;
			case 0:
				return;
			default:
				break;
		}
	}
}
