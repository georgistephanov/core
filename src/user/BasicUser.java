package user;

import core.Engine;
import data.MySQLAccess;
import lib.GeneralHelperFunctions;
import lib.payment.VisaCard;
import product.*;

public class BasicUser implements User {
	private long accountNumber;
	private static int number_of_accounts = 1;

	private String username;
	private String accountType = "Basic user";

	private VisaCard card = new VisaCard();
	private Cart cart = new Cart(card);

	private int login_max_try = 3;

	public boolean authorised = false;


	/* ============== CONSTRUCTORS ============== */
	public BasicUser() {
		String[] info = UserOperations.createUser();
		MySQLAccess db = MySQLAccess.getMySQLObject();

		if (info[0] != null && info[1] != null) {
			if (db.registerUser(info[0], info[1])) {
				accountNumber = User.account_num + db.getIDFromUsername(info[0]);
				this.username = info[0];
				authorised = true;
			} else {
				System.out.println("(BasicUser: BasicUser()) Unable to register user.");
			}
		}
	}

	public BasicUser(boolean hasAccount) {
		if (hasAccount) {
			initUser();
		}
		else {
			System.out.println("BasicUser: Invalid call to constructor with parameter: false");
			Engine.terminateApplication();
		}
	}

	private void initUser() {
		if (login_max_try > 0) {
			String info[] = askForUsernameAndPassword();

			if (UserOperations.login(info[0], info[1])) {
				accountNumber = User.account_num + MySQLAccess.getMySQLObject().getIDFromUsername(info[0]);
				this.username = info[0];
				authorised = true;
			} else {
				login_max_try--;
				System.out.println("\n\n" + login_max_try + " tries left.");
				initUser();
			}
		} else {
			Engine.terminateApplication();
		}
	}

	private String[] askForUsernameAndPassword() {
		String info[] = new String[2];

		System.out.println("Username: ");
		info[0] = Engine.inputScanner.next();

		System.out.println("Password: ");
		info[1] = Engine.inputScanner.next();

		return info;
	}


	/* ============== GETTERS ============== */
	long getAccountNumber() { return this.accountNumber; }
	String getUsername() { return this.username; }
	String getAccountType() { return this.accountType; }



	/* ============== PUBLIC METHODS ============== */
	public void addToCart(Product p) {
		cart.addToCart(p);
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

	public void userCartMenu() {
		this.cart.cartMenu();
	}
}
