package data;
import core.Engine;
import lib.payment.VisaCard;
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
			System.out.println(e.toString());
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
			System.out.println("(MySQLAccess: getUsernames()) " + e.toString());
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
			System.out.println(e.toString());
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
			System.out.println("(MySQLAccess: changePassword) " + e.toString());
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
			System.out.println("(MySQLAccess: registerUser()) Error while trying to add the user to the database.");
			System.out.println(e.toString());
			Engine.terminateApplication();
			return false;
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
				System.out.println("(MySQLAccess: getIDFromUsername) No such user exists.");
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
				System.out.println("(MySQLAccess: addProductToTheDatabase) " + e.toString());
			} finally {
				_close();
			}
		} else {
			System.out.println("Product with such name already exists!");
		}

		return false;
	}

	public boolean removeProduct(int id) {
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

				return true;
			}
			catch (Exception e) {
				_logErrorMessage(e, "removeProduct");
			}
			finally {
				_close();
			}
		} else {
			System.out.println("ID: " + id);
			System.out.println("Product with such ID does not exist.");
		}

		return false;
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
					return new Product(id, name, price, quantity);
			}
			else
				System.out.println("Couldn't get the product information from the database.");

			return null;
		}
		catch (Exception e) {
			System.out.println("(MySQLAccess: getProductFromName) " + e.toString());
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
			_logErrorMessage(e, "getFirstName");
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
			_logErrorMessage(e, "getLastName");
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
			_logErrorMessage(e, "changeFirstName");
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
			_logErrorMessage(e, "changeLastName");
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
			_logErrorMessage(e, "getCardNumber");
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
			_logErrorMessage(e, "getCardBalance");
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
			_logErrorMessage(e, "updateCardBalance");
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

			preparedStatement = connect.prepareStatement("INSERT INTO cards VALUES (?, ?, ?)");
			preparedStatement.setInt(1, user_id);
			preparedStatement.setString(2, cardNum);
			preparedStatement.setDouble(3, 3000.);
			preparedStatement.executeUpdate();

			System.out.println("The card has been successfully added.");
			return true;
		}
		catch (Exception e) {
			_logErrorMessage(e, "addCard");
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

					System.out.println("User 123_456_" + id + " has been given premium status successfully.");
				}
				catch (Exception e) {
					_logErrorMessage(e, "makeUserPremium");
				}
				finally {
					_close();
				}
			} else {
				System.out.println("No such user exists.");
			}
		}
	}

	/* ======== PRIVATE METHODS ======== */

	private void _readDatabase() throws Exception {
		try {
			// This will load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");

			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");

			// Statements allow to issue SQL queries to the DB
			statement = connect.createStatement();

			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("SELECT * FROM user");
			_writeResultSet(resultSet);

			System.out.println("\n------------------------\n");

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("INSERT INTO user VALUES (default, ?, ?, ?)");
			preparedStatement.setString(1, "JoseJR");
			preparedStatement.setString(2,"papa-papa-papa");
			preparedStatement.setInt(3, 11);
			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("SELECT * FROM user");
			resultSet = preparedStatement.executeQuery();
			_writeResultSet(resultSet);

			System.out.println("\n------------------------\n");

			// Remove again the inserted user
			/*	preparedStatement = connect.prepareStatement("DELETE FROM usernames WHERE username= ? ;");
				preparedStatement.setString(1, "georgi");
				preparedStatement.executeUpdate();
			*/

			resultSet = statement.executeQuery("SELECT * FROM user");
			_writeResultSet(resultSet);
			//_writeMetaData(resultSet);

		}
		catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			_close();
		}
	}

	private void _writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}

	private void _writeResultSet(ResultSet resultSet) throws SQLException {
		// ResultSet is initially before the first data set
		while (resultSet.next()) {
			// It is possible to get the columns via name
			// also possible to get the columns via the column number
			// which starts at 1 e.g. resultSet.getString(2);
			String username = resultSet.getString("username");
			String password = resultSet.getString("password");
			int cuteness = resultSet.getInt("cuteness");
			System.out.println("Username: " + username);
			System.out.println("Password: " + password);
			System.out.println("Cuteness: " + cuteness + "\n");
		}
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
			connect = _prepareConnection("users");
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user WHERE id=" + id);

			if (resultSet.next()) {
				return true;
			}
		}
		catch (Exception e) {
			_logErrorMessage(e, "_userExists : id");
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

			products.add(new Product(id, productName, price, quantityAv));
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
			System.out.println(e.toString());
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
			System.out.println("(MySQLAccess: _productAlreadyExists) " + e.toString());
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
			System.out.println("(MySQLAccess: _productAlreadyExists) " + e.toString());
		}
		finally {
			_close();
		}

		return false;
	}

	private Connection _prepareConnection(String databaseToUse) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/" + databaseToUse + "?autoReconnect=true&useSSL=false", "root", "");
	}

	private void _logErrorMessage(Exception e, String nameOfMethod) {
		System.out.println("(MySQLAccess : " + nameOfMethod + ") " + e.toString());
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
			System.out.println(e.toString());
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
			System.out.println(e.toString());
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
			System.out.println(e.toString());
		}
		finally {
			_close();
		}

		return premium;
	}
}