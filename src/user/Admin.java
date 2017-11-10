package user;

public class Admin extends User {

	Admin(String username) {
		setObjectVariables(username, User.AccountType.ADMIN);
	}

	public void initialiseMainMenu() {
		new UserMenu(this).initialiseMainMenu();
	}

	public String toString() { return super.toString(); }
}
