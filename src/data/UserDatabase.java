package data;

import product.Product;

import java.util.ArrayList;

public class UserDatabase extends Database {

	public UserDatabase() {
		super("users");
	}

	/* These methods help the UserFactory */
	public boolean isAdmin(String username) {
		boolean admin = false;

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT admin FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("admin")) == 1)
					admin = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "isAdmin");
		}
		finally {
			_close();
		}

		return admin;
	}
	public boolean isManager(String username) {
		boolean manager = false;

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT manager FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("manager")) == 1)
					manager = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "isManagerr");
		}
		finally {
			_close();
		}

		return manager;
	}
	public boolean isPremium(String username) {
		boolean premium = false;

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT premium FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("premium")) == 1)
					premium = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "isPremium");
		}
		finally {
			_close();
		}

		return premium;
	}


	/* =============== User Methods =============== */
	public boolean login(String username, String password) {

		boolean usernameExists = _usernameExists(username);

		if (usernameExists) {
			if (passwordMatch(username, password)) {
				System.out.println("\nLogging you in...");
				return true;
			}
		} else {
			System.out.println("No such username exists.");
		}

		return false;
	}
	public boolean registerUser(String username, String password) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			preparedStatement = connect.prepareStatement("INSERT INTO user VALUES (default, ?, ?, ?, false, false ,false)");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setInt(3, 0);
			preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			System.err.println("\nError while trying to add the user to the database.");
			logger.logError(e, "Database", "registerUser");
		}
		finally {
			_close();
		}

		return true;
	}
	public void makeUserPremium(int id) {
		if (id != 0) {
			if (_userExists(id)) {
				try {
					connect = _prepareConnection();
					preparedStatement = connect.prepareStatement("UPDATE user SET premium=true WHERE id=?");
					preparedStatement.setInt(1, id);
					preparedStatement.executeUpdate();

					System.out.println("User 100_123_" + id + " has been given premium status successfully.");
				}
				catch (Exception e) {
					logger.logError(e, "Database", "makeUserPremium");
				}
				finally {
					_close();
				}
			} else {
				System.out.println("No such user exists.");
			}
		}
	}
	public boolean addCard(int user_id, String cardNum) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT id FROM card WHERE cardNumber=" + cardNum);

			if (resultSet.next()) {
				System.out.println("\nThis card already belongs to another user!\n");
				return false;
			}

			preparedStatement = connect.prepareStatement("INSERT INTO card VALUES (?, ?, ?)");
			preparedStatement.setInt(1, user_id);
			preparedStatement.setString(2, cardNum);
			preparedStatement.setDouble(3, 3000.);
			preparedStatement.executeUpdate();

			System.out.println("The card has been successfully added.");
			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "addCard");
		}
		finally {
			_close();
		}

		return false;
	}
	public boolean passwordMatch(String username, String password) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT password FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				String dbPassword = resultSet.getString("password");

				if(password.equals(dbPassword)) {
					return true;
				} else {
					System.out.println("Wrong password. Try again!");
				}
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "passwordMatch");
		}
		finally {
			_close();
		}

		return false;
	}
	public boolean changePassword(String username, String newPassword) {
		try {
			connect = _prepareConnection();

			preparedStatement = connect.prepareStatement("UPDATE user SET password=? WHERE username=?");
			preparedStatement.setString(1, newPassword);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "changePassword");
		}
		finally {
			_close();
		}

		return false;
	}

	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT username FROM user");
			usernames = _createStringArrayListFromResultSet(resultSet, "username");
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getUsernames");
		}
		finally {
			_close();
		}

		return usernames;
	}
	public String getFirstName(int id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT firstName FROM user_info WHERE id=" + id);

			if (resultSet.next())
				return resultSet.getString("firstName");
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getFirstName");
		}
		finally {
			_close();
		}

		return "";
	}
	public String getLastName(int id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT lastName FROM user_info WHERE id=" + id);

			if (resultSet.next())
				return resultSet.getString("lastName");
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getLastName");
		}
		finally {
			_close();
		}

		return "";
	}
	public boolean changeFirstName(int id, String newName) {
		try {
			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("UPDATE user_info SET firstName=? WHERE id=?");
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "changeFirstName");
		}
		finally {
			_close();
		}

		return false;
	}
	public boolean changeLastName(int id, String newName) {
		try {
			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("UPDATE user_info SET lastName=? WHERE id=?");
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "changeLastName");
		}
		finally {
			_close();
		}

		return false;
	}

	public int getIDFromUsername(String username) {
		int id = -1;

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT id FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				id = Integer.parseInt(resultSet.getString("id"));
			} else {
				System.out.println("No such user exists.");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		finally {
			_close();
		}

		return id;
	}

	private boolean _usernameExists(String username) {
		ArrayList<String> usernames = getUsernames();

		for (String u : usernames) {
			if (u.equals(username.trim())) {
				return true;
			}
		}

		return false;
	}
	private boolean _userExists(int id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user WHERE id=" + id);

			if (resultSet.next()) {
				return true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "_userExists");
		}
		finally {
			_close();
		}

		return false;
	}



	/* =============== Card Methods =============== */
	public String getCardNumber(int user_id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT cardNumber FROM card WHERE id=" + user_id);

			if (resultSet.next()) {
				return resultSet.getString("cardNumber");
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getCardNumber");
		}
		finally {
			_close();
		}

		return null;
	}
	public double getCardBalance(int user_id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT balance FROM card WHERE id=" + user_id);

			if (resultSet.next()) {
				return resultSet.getDouble("balance");
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getCardBalance");
		}
		finally {
			_close();
		}

		return -1;
	}

	// TODO: Try to encapsulate this so that it couldn't be manipulated from outside
	public boolean updateCardBalance(String cardNumber, double newBalance) {
		try {
			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("UPDATE card SET balance=? WHERE cardNumber=?");
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setString(2, cardNumber);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "updateCardBalance");
		}
		finally {
			_close();
		}

		return false;
	}



	/* =============== Order Methods =============== */
	public void printPreviousOrders(int id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM orders WHERE user_id=" + id);

			System.out.println("\nPrevious orders:\n"
					+ "\t| Order number |    Date    |   Price\t|" );
			while (resultSet.next()) {
				System.out.println("\t| #" + 187_453_00 + resultSet.getInt("id")
						+ "   | " + resultSet.getDate("date")
						+ " |  $" + resultSet.getDouble("amount_paid") + "\t|");
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "printPreviousOrders");
		}
		finally {
			_close();
		}
	}
	public void printFullPreviousOrder(int orderNumber) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM order_products WHERE order_id=" + orderNumber);

			if (resultSet.isBeforeFirst()) {
				// TODO: These could be mapped together
				ArrayList<Integer> productIDs = new ArrayList<>();
				ArrayList<Double> productPrices = new ArrayList<>();

				// These should be filled here as another database connection is created
				// in the for loop via a function call to getProductFromID();
				while (resultSet.next()) {
					productIDs.add(resultSet.getInt("product_id"));
					productPrices.add(resultSet.getDouble("price"));
				}

				// Print the full order information
				ProductDatabase productDatabase = new ProductDatabase();

				System.out.println("Order #" + 187_453_00 + orderNumber);
				for (int i = 0; i < productIDs.size(); i++) {
					Product product = productDatabase.getProductFromID(productIDs.get(i));
					if (product != null)
						System.out.println("\t" + product.getName() + "\t\t$" + productPrices.get(i));
				}

			} else {
				System.out.println("There is no order with such number.");
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "printFullPreviousOrder");
		}
		finally {
			_close();
		}
	}

}