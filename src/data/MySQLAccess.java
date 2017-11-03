package data;

import lib.Logger;
import product.Product;
import java.util.ArrayList;
import java.sql.*;

// This class implements the Singleton pattern
public final class MySQLAccess {
	private static MySQLAccess db = new MySQLAccess();

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	private MySQLAccess() {}
	public static MySQLAccess getMySQLObject() { return db; }

	private Logger logger = Logger.getInstance();


	/* ======== PUBLIC METHODS ======== */

	public ArrayList<Product> getProductsFromDatabase() {
		ArrayList<Product> products = new ArrayList<>();

		try {
			connect = _prepareConnection("products");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT * FROM product");

			products = _createProductArrayListFromResultSet(resultSet);
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getProductsFromDatabase");
		}
		finally {
			_close();
		}

		return products;
	}

	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();

		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT username FROM user");
			usernames = _createStringArrayListFromResultSet(resultSet);
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getUsernames");
		}
		finally {
			_close();
		}

		return usernames;
	}

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

	public boolean passwordMatch(String username, String password) {
		try {
			connect = _prepareConnection("users");
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
			logger.logError(e, "MySQLAccess", "passwordMatch");
		}
		finally {
			_close();
		}

		return false;
	}

	public boolean changePassword(String username, String newPassword) {
		try {
			connect = _prepareConnection("users");

			preparedStatement = connect.prepareStatement("UPDATE user SET password=? WHERE username=?");
			preparedStatement.setString(1, newPassword);
			preparedStatement.setString(2, username);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "changePassword");
		}
		finally {
			_close();
		}

		return false;
	}

	public boolean registerUser(String username, String password) {
		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();

			preparedStatement = connect.prepareStatement("INSERT INTO user VALUES (default, ?, ?, ?, false, false ,false)");
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);
			preparedStatement.setInt(3, 0);
			preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			System.err.println("\nError while trying to add the user to the database.");
			logger.logError(e, "MySQLAccess", "registerUser");
		}
		finally {
			_close();
		}

		return true;
	}

	public int getIDFromUsername(String username) {
		int id = -1;

		try {
			connect = _prepareConnection("users");
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

	public boolean addProduct(String name, double price, int quantity) {
		if (!_productAlreadyExists(name)) {
			try {
				connect = _prepareConnection("products");

				preparedStatement = connect.prepareStatement("INSERT INTO product VALUES (default, ?, ?, ?)");
				preparedStatement.setString(1, name);
				preparedStatement.setDouble(2, price);
				preparedStatement.setInt(3, quantity);

				preparedStatement.executeUpdate();

				return true;
			} catch (Exception e) {
				logger.logError(e, "MySQLAccess", "addProduct");
			} finally {
				_close();
			}
		} else {
			System.out.println("Product with such name already exists!");
		}

		return false;
	}

	public void removeProduct(int id) {
		if(_productExists(id)) {
			try {
				connect = _prepareConnection("products");
				statement = connect.createStatement();
				resultSet = statement.executeQuery("SELECT name FROM product WHERE id=" + id);
				String productName = null;

				if (resultSet.next()) {
					productName = resultSet.getString("name");
				}

				preparedStatement = connect.prepareStatement("DELETE FROM product WHERE id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();

				if (productName != null)
					System.out.println(productName + " successfully removed from the database!");
				else
					System.out.println("The product was successfully removed from the database");

			}
			catch (Exception e) {
				logger.logError(e, "MySQLAccess", "removeProduct");
			}
			finally {
				_close();
			}
		} else {
			System.out.println("ID: " + id);
			System.out.println("Product with such ID does not exist.");
		}
	}

	public Product getProductFromName(String name) {
		try {
			connect = _prepareConnection("products");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM product WHERE name='" + name + "'");

			if (resultSet.next()) {
				int id = resultSet.getInt("id");
				double price = resultSet.getDouble("price");
				int quantity = resultSet.getInt("quantityAvailable");

				if (id != 0 && price != 0 && quantity != 0)
					return new Product.Builder(id, name, price, quantity).build();
			}
			else
				System.out.println("Couldn't get the product information from the database.");

			return null;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getProductFromName");
		}
		finally {
			_close();
		}

		return null;
	}

	public String getFirstName(int id) {
		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT firstName FROM user_info WHERE id=" + id);

			if (resultSet.next())
				return resultSet.getString("firstName");
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getFirstName");
		}
		finally {
			_close();
		}

		return "";
	}

	public String getLastName(int id) {
		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT lastName FROM user_info WHERE id=" + id);

			if (resultSet.next())
				return resultSet.getString("lastName");
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getLastName");
		}
		finally {
			_close();
		}

		return "";
	}

	public boolean changeFirstName(int id, String newName) {
		try {
			connect = _prepareConnection("users");
			preparedStatement = connect.prepareStatement("UPDATE user_info SET firstName=? WHERE id=?");
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "changeFirstName");
		}
		finally {
			_close();
		}

		return false;
	}

	public boolean changeLastName(int id, String newName) {
		try {
			connect = _prepareConnection("users");
			preparedStatement = connect.prepareStatement("UPDATE user_info SET lastName=? WHERE id=?");
			preparedStatement.setString(1, newName);
			preparedStatement.setInt(2, id);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "changeLastName");
		}
		finally {
			_close();
		}

		return false;
	}

	public String getCardNumber(int user_id) {
		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT cardNumber FROM card WHERE id=" + user_id);

			if (resultSet.next()) {
				return resultSet.getString("cardNumber");
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getCardNumber");
		}
		finally {
			_close();
		}

		return null;
	}

	public double getCardBalance(int user_id) {
		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT balance FROM card WHERE id=" + user_id);

			if (resultSet.next()) {
				return resultSet.getDouble("balance");
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getCardBalance");
		}
		finally {
			_close();
		}

		return -1;
	}

	public boolean updateCardBalance(String cardNumber, double newBalance) {
		try {
			connect = _prepareConnection("users");
			preparedStatement = connect.prepareStatement("UPDATE card SET balance=? WHERE cardNumber=?");
			preparedStatement.setDouble(1, newBalance);
			preparedStatement.setString(2, cardNumber);
			preparedStatement.executeUpdate();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "updateCardBalance");
		}
		finally {
			_close();
		}

		return false;
	}

	public boolean addCard(int user_id, String cardNum) {
		try {
			connect = _prepareConnection("users");
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
			logger.logError(e, "MySQLAccess", "addCard");
		}
		finally {
			_close();
		}

		return false;
	}

	public void makeUserPremium(int id) {
		if (id != 0) {
			if (_userExists(id)) {
				try {
					connect = _prepareConnection("users");
					preparedStatement = connect.prepareStatement("UPDATE user SET premium=true WHERE id=?");
					preparedStatement.setInt(1, id);
					preparedStatement.executeUpdate();

					System.out.println("User 100_123_" + id + " has been given premium status successfully.");
				}
				catch (Exception e) {
					logger.logError(e, "MySQLAccess", "makeUserPremium");
				}
				finally {
					_close();
				}
			} else {
				System.out.println("No such user exists.");
			}
		}
	}

	public void printPreviousOrders(int id) {
		try {
			connect = _prepareConnection("users");
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
			logger.logError(e, "MySQLAccess", "printPreviousOrders");
		}
		finally {
			_close();
		}
	}

	public void printFullPreviousOrder(int orderNumber) {
		try {
			connect = _prepareConnection("users");
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
				System.out.println("Order #" + 187_453_00 + orderNumber);
				for (int i = 0; i < productIDs.size(); i++) {
					Product product = getProductFromID(productIDs.get(i));
					if (product != null)
						System.out.println("\t" + product.getName() + "\t\t$" + productPrices.get(i));
				}

			} else {
				System.out.println("There is no order with such number.");
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "printFullPreviousOrder");
		}
		finally {
			_close();
		}
	}

	/* ============ PRIVATE METHODS ============ */

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
			connect = _prepareConnection("user");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user WHERE id=" + id);

			if (resultSet.next()) {
				return true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "_userExists");
		}
		finally {
			_close();
		}

		return false;
	}

	private ArrayList<Product> _createProductArrayListFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<Product> products = new ArrayList<>();

		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String productName = resultSet.getString("name");
			float price = resultSet.getFloat("price");
			int quantityAv = resultSet.getInt("quantityAvailable");

			products.add(new Product.Builder(id, productName, price, quantityAv).build());
		}

		return products;
	}

	private ArrayList<String> _createStringArrayListFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<String> arrayList = new ArrayList<>();

		while (resultSet.next()) {
			arrayList.add(resultSet.getString("username"));
		}

		return arrayList;
	}

	private void _close() {
		// You need to _close the resultSet
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {
			logger.logError(e, "MySQLAccess", "_close");
		}
	}

	private boolean _productAlreadyExists(String name) {
		try {
			connect = _prepareConnection("products");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT name FROM product");

			while (resultSet.next()) {
				if (name.equalsIgnoreCase(resultSet.getString("name")))
					return true;
			}

			return false;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "_productAlreadyExists");
		}
		finally {
			_close();
		}

		return true;
	}

	private boolean _productExists(int id) {
		try {
			connect = _prepareConnection("products");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT id FROM product");

			while (resultSet.next()) {
				if (id == resultSet.getInt("id"))
					return true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "_productExists");
		}
		finally {
			_close();
		}

		return false;
	}

	private Product getProductFromID(int id) {
		try {
			connect = _prepareConnection("products");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM product WHERE id=" + id);

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				double price = resultSet.getDouble("price");
				int quantity = resultSet.getInt("quantityAvailable");

				if (id != 0 && price != 0 && quantity != 0)
					return new Product.Builder(id, name, price, quantity).build();
			}
			else
				System.out.println("Couldn't get the product information from the database.");

			return null;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "getProductFromID");
		}
		finally {
			_close();
		}

		return null;
	}

	private Connection _prepareConnection(String databaseToUse) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/" + databaseToUse + "?autoReconnect=true&useSSL=false", "root", "");
	}

	/* These methods help the UserFactory */
	public boolean isAdmin(String username) {
		boolean admin = false;

		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT admin FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("admin")) == 1)
					admin = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "isAdmin");
		}
		finally {
			_close();
		}

		return admin;
	}

	public boolean isManager(String username) {
		boolean manager = false;

		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT manager FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("manager")) == 1)
					manager = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "isManagerr");
		}
		finally {
			_close();
		}

		return manager;
	}

	public boolean isPremium(String username) {
		boolean premium = false;

		try {
			connect = _prepareConnection("users");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT premium FROM user WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("premium")) == 1)
					premium = true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "isPremium");
		}
		finally {
			_close();
		}

		return premium;
	}


	public boolean isRunning() {
		try {
			_prepareConnection("users");
			_prepareConnection("products");

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "MySQLAccess", "isRunning");
		}
		finally {
			_close();
		}

		return false;
	}
}