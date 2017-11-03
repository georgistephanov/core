package user;

// TODO: Add system status for the admin
public class Admin extends User {

	public Admin(String username) {
		setObjectVariables(username, User.AccountType.ADMIN);
	}

	public void initialiseMainMenu() {

	}
}
