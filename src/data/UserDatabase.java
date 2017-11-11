package data;

import core.Engine;
import lib.GeneralHelperFunctions;
import product.Product;
import user.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;


public class UserDatabase extends Database {

	public UserDatabase() {
		// The first parameter is the database to be used
		// The second parameter is the filename which will be used by the logger
		super("users", "UserDatabase");
	}

	/*  These methods are used by UserFactory to determine whether
	 *   the user is premium user, manager or admin in order to create
	 *   the correct user object. They take the username with which
	 *   the user has been logged in.
	 */
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
			logError(e, "isAdmin");
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
			logError(e, "isManagerr");
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
			logError(e, "isPremium");
		}
		finally {
			_close();
		}

		return premium;
	}


	/* =============== User Methods =============== */
	public boolean login(String username, String password) {

		boolean usernameExists = _userExists(username);

		if (usernameExists) {
			if (passwordMatch(username, password)) {
				System.out.println("\nLogging you in...");

				// Register the start of the session
				try {
					connect = _prepareConnection();
					preparedStatement = connect.prepareStatement("INSERT INTO session VALUES (?, default, NULL)");
					preparedStatement.setInt(1, getIDFromUsername(username));
					//preparedStatement.setDate(2, new java.sql.Date(System.currentTimeMillis()));
					preparedStatement.executeUpdate();
				}
				catch (Exception e) {
					logError(e, "login");
				}
				finally {
					_close();
				}

				return true;
			}
		} else {
			System.out.println("No such username exists.");
		}

		return false;
	}
	public void logout(int id) {
		try {
			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("UPDATE session SET end=default WHERE id=? ORDER BY start DESC LIMIT 1");
			//preparedStatement.setDate(1, new java.sql.Date(System.currentTimeMillis()));
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();
		}
		catch (Exception e) {
			logError(e, "logout");
		}
		finally {
			_close();
		}
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
			logError(e, "registerUser");
		}
		finally {
			_close();
		}

		return true;
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
			logError(e, "addCard");
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
			logError(e, "passwordMatch");
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
			logError(e, "changePassword");
		}
		finally {
			_close();
		}

		return false;
	}
	public void deleteUser(int id) {
		try {
			if (_userExists(id)) {
				connect = _prepareConnection();

				preparedStatement = connect.prepareStatement("DELETE FROM user WHERE id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();

				preparedStatement = connect.prepareStatement("DELETE FROM user_info WHERE id=?");
				preparedStatement.setInt(1, id);
				preparedStatement.executeUpdate();

				System.out.println("User 123_456_" + id + " successfully deleted.");
			} else {
				System.out.println("No user with this id exists.");
			}
		}
		catch (Exception e) {
			logError(e, "deleteUser");
		}
		finally {
			_close();
		}
	}

	public void makeUserPremium(int id) {
		giveUserPrivileges(id, 1);
	}
	public void makeUserManager(int id) {
		giveUserPrivileges(id, 2);
	}
	public void makeUserAdmin(int id) {
		giveUserPrivileges(id, 3);
	}
	private void giveUserPrivileges(int id, int option) {
		if (id > 0) {
			if (_userExists(id)) {
				try {
					connect = _prepareConnection();

					String status = "";
					switch (option) {
						case 1:
							status = "premium";
							preparedStatement = connect.prepareStatement("UPDATE user SET premium=true WHERE id=?");
							break;
						case 2:
							status = "manager";
							preparedStatement = connect.prepareStatement("UPDATE user SET manager=true WHERE id=?");
							break;
						case 3:
							status = "admin";
							preparedStatement = connect.prepareStatement("UPDATE user SET admin=true WHERE id=?");
							break;
						default:
							System.out.println("Wrong option parameter provided.");
							return;
					}
					preparedStatement.setInt(1, id);
					preparedStatement.executeUpdate();

					System.out.println("User 100_123_" + id + " has been given " + status + " status successfully.");
				} catch (Exception e) {
					logError(e, "giveUserPrivileges");
				} finally {
					_close();
				}
			} else {
				System.out.println("No user exists with this id");
			}
		} else {
			System.out.println("Invalid input.");
		}
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
			logError(e, "getUsernames");

			// This is crucial for the login process in order to prevent exposing system information.
			Engine.getInstance().terminateApplication();
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
			logError(e, "getFirstName");
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
			logError(e, "getLastName");
		}
		finally {
			_close();
		}

		return "";
	}
	public boolean changeFirstName(int id, String newName) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user_info WHERE id=" + id);

			if (resultSet.next()) {
				preparedStatement = connect.prepareStatement("UPDATE user_info SET firstName=? WHERE id=?");
				preparedStatement.setString(1, newName);
				preparedStatement.setInt(2, id);
				preparedStatement.executeUpdate();
			} else {
				preparedStatement = connect.prepareStatement("INSERT INTO user_info VALUES (?, ?, default)");
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, newName);
				preparedStatement.executeUpdate();
			}
			return true;
		}
		catch (Exception e) {
			logError(e, "changeFirstName");
		}
		finally {
			_close();
		}

		return false;
	}
	public boolean changeLastName(int id, String newName) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM user_info WHERE id=" + id);

			if (resultSet.next()) {
				preparedStatement = connect.prepareStatement("UPDATE user_info SET lastName=? WHERE id=?");
				preparedStatement.setString(1, newName);
				preparedStatement.setInt(2, id);
				preparedStatement.executeUpdate();
			} else {
				preparedStatement = connect.prepareStatement("INSERT INTO user_info VALUES (?, default, ?)");
				preparedStatement.setInt(1, id);
				preparedStatement.setString(2, newName);
				preparedStatement.executeUpdate();
			}

			return true;
		}
		catch (Exception e) {
			logError(e, "changeLastName");
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
			logError(e, "getIDFromUsername");
		}
		finally {
			_close();
		}

		return id;
	}
	public void getUserByUsernameOrID(String query) {
		int id = -1;
		String username = "";

		// Check if the input is Id or username
		if (query.matches("^\\d+$")) {
			id = Integer.parseInt(query);
		} else {
			username = query;
		}

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			// Execute the correct query depending on the user input (Id or username)
			if (id != -1) {
				resultSet = statement.executeQuery("SELECT * FROM user WHERE id=" + id);
			} else {
				resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + username + "'");
			}

			// Getting the information from the user table
			if (resultSet.next()) {
				id = resultSet.getInt("id");
				String accountType;

				if (Integer.parseInt(resultSet.getString("admin")) == 1) {
					accountType = "Admin";
				}
				else if (Integer.parseInt(resultSet.getString("manager")) == 1) {
					accountType = "Manager";
				}
				else if (Integer.parseInt(resultSet.getString("premium")) == 1) {
					accountType = "Premium user";
				}
				else {
					accountType = "Basic user";
				}

				// These are the string variables that are going to be used to print the block message
				String userID = "User id: " + id;
				String userName = "Username: " + resultSet.getString("username");
				String userFirstName = "First name: ";
				String userLastName = "Last name: ";
				String userAccountType = "Account type: " + accountType;
				String userLastVisit = "\nLast visit: ";
				String userLastVisitLength = "Session length: ";

				// Getting the information from the user_info table
				resultSet = statement.executeQuery("SELECT * FROM user_info WHERE id=" + id);

				if (resultSet.next()) {
					if (resultSet.getString("firstName") != null) {
						userFirstName += resultSet.getString("firstName");
					}
					if (resultSet.getString("lastName") != null) {
						userLastName += resultSet.getString("lastName") + "\n";
					}
				}

				// Get the last visited information if available
				resultSet = statement.executeQuery("SELECT * FROM session WHERE id=" + id + " ORDER BY start DESC");
				if (resultSet.next()) {
					java.text.DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					java.util.Date lastVisit = resultSet.getTimestamp("start");

					if (lastVisit == null) {
						lastVisit = resultSet.getDate("start");
					}

					userLastVisit += dateFormat.format(lastVisit);

					String timeString;

					java.util.Date lastVisitLogout = resultSet.getTimestamp("end");
					if (lastVisitLogout != null) {
						int totalSeconds = (int) (lastVisitLogout.getTime() - lastVisit.getTime()) / 1000;
						int hours = totalSeconds / 3600;
						int minutes = (totalSeconds % 3600) / 60;
						int seconds = totalSeconds % 60;

						timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
					} else {
						if (id == 18) {
							timeString = "Current";
						} else {
							timeString = "Not recorded";
						}
					}

					userLastVisitLength += timeString;

				} else {
					userLastVisit += "Never";
					userLastVisitLength += "Never";
				}

				// Printing the user information in a block message
				GeneralHelperFunctions.printBlockMessage(userID, userName, userFirstName, userLastName, userAccountType, userLastVisit, userLastVisitLength);
			} else {
				System.out.println("\nNo such user exists.");
			}
		}
		catch (Exception e) {
			logError(e, "getUserByID");
		}
		finally {
			_close();
		}
	}

	private boolean _userExists(String username) {
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
			logError(e, "_userExists");
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
			logError(e, "getCardNumber");
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
			logError(e, "getCardBalance");
		}
		finally {
			_close();
		}

		return -1;
	}
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
			logError(e, "updateCardBalance");
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
			logError(e, "printPreviousOrders");
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
				/*
				 * This class maps together the product id and its price when bought.
				 * It stores them into two separate queues. The set method uses the add()
				 * methods of the queue to add the value to the corresponding field.
				 * The get method uses the remove() method of the queue which returns
				 * and removes the value from the queue. This class is specifically
				 * designed for this method, but could be reused anywhere else where
				 * needed.
				 */
				class ProductInfo<T, V> {
					private Queue<T> productIDs;
					private Queue<V> productPrices;

					private ProductInfo() { productIDs = new LinkedList<>(); productPrices = new LinkedList<>();	}

					private void addProductID(T productID) { productIDs.add(productID); }
					private void addProductPrice(V productPrice) { productPrices.add(productPrice); }
					private T getProductID() { return productIDs.remove(); }
					private V getProductPrice() { return productPrices.remove(); }
					private int getNumberOfElements() { return productIDs.size(); }
				}

				ProductInfo<Integer, Double> productInfo = new ProductInfo<>();

				// These should be filled here as another database connection is created
				// in the for loop via a function call to getProductFromID();
				while (resultSet.next()) {
					Double pricePaid = (double) Math.round(resultSet.getDouble("price") * 100) / 100;
					productInfo.addProductPrice(pricePaid);
					productInfo.addProductID(resultSet.getInt("product_id"));
				}

				// Print the full order information
				ProductDatabase productDatabase = new ProductDatabase();

				System.out.println("Order #" + 187_453_00 + orderNumber);
				while (productInfo.getNumberOfElements() > 0) {
					Product product = productDatabase.getProductFromID(productInfo.getProductID());
					if (product != null) {
						System.out.println("\t" + product.getName() + "\t\t$" + productInfo.getProductPrice());
					}
				}

			} else {
				System.out.println("There is no order with such number.");
			}
		}
		catch (Exception e) {
			logError(e, "printFullPreviousOrder");
		}
		finally {
			_close();
		}
	}
	public void addOrder(User user, double amountPaid, ArrayList<Product> items) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, 1);
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

			String orderDate = dateFormatter.format(calendar.getTime());

			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("INSERT INTO orders VALUES (default, ?, ?, ?)");
			preparedStatement.setInt(1, user.getID());
			preparedStatement.setDouble(2, amountPaid);
			preparedStatement.setString(3, orderDate);
			preparedStatement.executeUpdate();

			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
			int orderID = 0;
			if (resultSet.next()) {
				orderID = resultSet.getInt("id");
			}

			for (Product item : items) {
				double pricePaid = item.getPrice() * (100 - user.getCheckoutDiscountPercentage()) / 100;

				preparedStatement = connect.prepareStatement("INSERT INTO order_products VALUES (?, ?, ?)");
				preparedStatement.setInt(1, orderID);
				preparedStatement.setInt(2, item.getBaseID());
				preparedStatement.setDouble(3, pricePaid);
				preparedStatement.executeUpdate();
			}
		}
		catch (Exception e) {
			logError(e, "addOrder");
		}
	}
	public void printPreviousOrders() {
		try {
			int numOrdersPrinted = 20;

			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM orders");

			System.out.println("\nPrevious orders:\n"
					+ "\t|    User    | Order number |    Date    |   Price\t|" );
			while (resultSet.next() && numOrdersPrinted > 0) {
				System.out.println("\t| " + 123_456_00 + resultSet.getInt("user_id") + " | #" + 187_453_00 + resultSet.getInt("id")
						+ "   | " + resultSet.getDate("date")
						+ " |  $" + resultSet.getDouble("amount_paid") + "\t|");
				numOrdersPrinted--;
			}
		}
		catch (Exception e) {
			logError(e, "printPreviousOrders");
		}
		finally {
			_close();
		}
	}

}
