package data;
import core.Engine;
import product.Product;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import java.sql.*;

// This class implements the Singleton pattern
public class MySQLAccess {
	private static MySQLAccess db = new MySQLAccess();

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;


	private MySQLAccess() {}
	public static MySQLAccess getMySQLObject() { return db; }


	public ArrayList<Product> getProductsFromDatabase() {
		ArrayList<Product> products = new ArrayList<>();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/products?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT * FROM product");

			products = createProductArrayListFromResultSet(resultSet);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		finally {
			close();
		}

		return products;
	}

	public ArrayList<String> getUsernames() {
		ArrayList<String> usernames = new ArrayList<>();

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT username FROM usernames");
			usernames = createStringArrayListFromResultSet(resultSet);
		}
		catch (Exception e) {
			System.out.println("(MySQLAccess: getUsernames()) " + e.toString());
		}
		finally {
			close();
		}

		return usernames;
	}

	public boolean login(String username, String password) {

		boolean usernameExists = usernameExists(username);

		if (usernameExists) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
				statement = connect.createStatement();
				resultSet = statement.executeQuery("SELECT password FROM usernames WHERE username=\"" + username + "\"");

				if (resultSet.next()) {
					String dbPassword = resultSet.getString("password");

					if (password.equals(dbPassword)) {
						System.out.println("\nPassword match.\nLogging you in...\n");
						return true;
					} else {
						System.out.println("Wrong password. Try again!");
						return false;
					}
				}

			} catch (Exception e) {
				System.out.println("(MySQLAccess: login()) " + e.toString());
			} finally {
				close();
			}
		} else {
			System.out.println("No such username exists.");
			return false;
		}

		System.out.println("(MySQLAccess: login()) Unable to authorise you. Please try again!");
		return false;
	}

	public boolean registerUser(String username, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			preparedStatement = connect.prepareStatement("INSERT INTO usernames VALUES (default, ?, ?, ?, false, false ,false)");
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
			close();
		}

		return true;
	}

	public int getIDFromUsername(String username) {
		int id = -1;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT id FROM usernames WHERE username=\"" + username + "\"");

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
			close();
		}

		return id;
	}

	private void readDatabase() throws Exception {
		try {
			// This will load the MySQL driver
			Class.forName("com.mysql.jdbc.Driver");

			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");

			// Statements allow to issue SQL queries to the DB
			statement = connect.createStatement();

			// Result set get the result of the SQL query
			resultSet = statement.executeQuery("SELECT * FROM usernames");
			writeResultSet(resultSet);

			System.out.println("\n------------------------\n");

			// PreparedStatements can use variables and are more efficient
			preparedStatement = connect.prepareStatement("INSERT INTO usernames VALUES (default, ?, ?, ?)");
			preparedStatement.setString(1, "JoseJR");
			preparedStatement.setString(2,"papa-papa-papa");
			preparedStatement.setInt(3, 11);
			preparedStatement.executeUpdate();

			preparedStatement = connect.prepareStatement("SELECT * FROM usernames");
			resultSet = preparedStatement.executeQuery();
			writeResultSet(resultSet);

			System.out.println("\n------------------------\n");

			// Remove again the inserted user
			/*	preparedStatement = connect.prepareStatement("DELETE FROM usernames WHERE username= ? ;");
				preparedStatement.setString(1, "georgi");
				preparedStatement.executeUpdate();
			*/

			resultSet = statement.executeQuery("SELECT * FROM usernames");
			writeResultSet(resultSet);
			//writeMetaData(resultSet);

		}
		catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			close();
		}
	}

	private void writeMetaData(ResultSet resultSet) throws SQLException {
		// Now get some metadata from the database
		// Result set get the result of the SQL query

		System.out.println("The columns in the table are: ");

		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
		}
	}

	private void writeResultSet(ResultSet resultSet) throws SQLException {
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

	private boolean usernameExists(String username) {
		ArrayList<String> usernames = getUsernames();

		for (String u : usernames) {
			if (u.equals(username.trim())) {
				return true;
			}
		}

		return false;
	}

	private ArrayList<Product> createProductArrayListFromResultSet(ResultSet resultSet) throws SQLException {
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

	private ArrayList<String> createStringArrayListFromResultSet(ResultSet resultSet) throws SQLException {
		ArrayList<String> arrayList = new ArrayList<>();

		while (resultSet.next()) {
			arrayList.add(resultSet.getString("username"));
		}

		return arrayList;
	}

	private void close() {
		// You need to close the resultSet
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


	public boolean isAdmin(String username) {
		boolean admin = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT admin FROM usernames WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("admin")) == 1)
					admin = true;
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		finally {
			close();
		}

		return admin;
	}

	public boolean isManager(String username) {
		boolean manager = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT manager FROM usernames WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("manager")) == 1)
					manager = true;
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		finally {
			close();
		}

		return manager;
	}

	public boolean isPremium(String username) {
		boolean premium = false;

		try {
			Class.forName("com.mysql.jdbc.Driver");

			connect = DriverManager.getConnection("jdbc:mysql://localhost/users?autoReconnect=true&useSSL=false", "root", "");
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT premium FROM usernames WHERE username=\"" + username + "\"");

			if (resultSet.next()) {
				if(Integer.parseInt(resultSet.getString("premium")) == 1)
					premium = true;			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		finally {
			close();
		}

		return premium;
	}
}