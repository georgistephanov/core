package user;

public class Admin extends User {

	public Admin(String username) {
		setObjectVariables(username, User.AccountType.ADMIN);
	}

	public void initialiseMainMenu() {

	}
}
