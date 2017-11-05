package data;

import product.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDatabase extends Database {

	public ProductDatabase() {
		super("products");
	}


	/* ================ Public Methods ================ */
	public ArrayList<Product> getProductsFromDatabase() {
		ArrayList<Product> products = new ArrayList<>();

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();

			resultSet = statement.executeQuery("SELECT * FROM product");

			products = _createProductArrayListFromResultSet(resultSet);
		}
		catch (Exception e) {
			logger.logError(e, "Database", "getProductsFromDatabase");
		}
		finally {
			_close();
		}

		return products;
	}
	Product getProductFromID(int id) {
		try {
			connect = _prepareConnection();
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
			logger.logError(e, "Database", "getProductFromID");
		}
		finally {
			_close();
		}

		return null;
	}
	public Product getProductFromName(String name) {
		try {
			connect = _prepareConnection();
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
			logger.logError(e, "Database", "getProductFromName");
		}
		finally {
			_close();
		}

		return null;
	}

	public boolean addProduct(String name, double price, int quantity) {
		if (!_productExists(name)) {
			try {
				connect = _prepareConnection();

				preparedStatement = connect.prepareStatement("INSERT INTO product VALUES (default, ?, ?, ?)");
				preparedStatement.setString(1, name);
				preparedStatement.setDouble(2, price);
				preparedStatement.setInt(3, quantity);

				preparedStatement.executeUpdate();

				return true;
			} catch (Exception e) {
				logger.logError(e, "Database", "addProduct");
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
				connect = _prepareConnection();
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
				logger.logError(e, "Database", "removeProduct");
			}
			finally {
				_close();
			}
		} else {
			System.out.println("ID: " + id);
			System.out.println("Product with such ID does not exist.");
		}
	}

	public void printProductSuggestionsFromSearchQuery(String query) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM product");

			String keywords[] = query.split(" ");
			ArrayList<Integer> foundProductsID = new ArrayList<>();

			while (resultSet.next()) {
				String productName = resultSet.getString("name");

				for (String keyword : keywords) {
					if (productName.contains(keyword)) {
						foundProductsID.add(resultSet.getInt("id"));
						break;
					}
				}
			}

			_printSearchQueryInformation(foundProductsID, query);
		}
		catch (Exception e) {
			logger.logError(e, "ProductDatabase", "printProductSuggestionsFromSearchQuery");
		}
		finally {
			_close();
		}
	}

	private void _printSearchQueryInformation(ArrayList<Integer> foundProducts, String query) {
		// TODO: Print the found results
	}


	/* ================ Private Methods ================ */

	private boolean _productExists(String name) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT name FROM product");

			while (resultSet.next()) {
				if (name.equalsIgnoreCase(resultSet.getString("name")))
					return true;
			}

			return false;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "_productExists");
		}
		finally {
			_close();
		}

		return true;
	}

	private boolean _productExists(int id) {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT id FROM product");

			while (resultSet.next()) {
				if (id == resultSet.getInt("id"))
					return true;
			}
		}
		catch (Exception e) {
			logger.logError(e, "Database", "_productExists");
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

}
