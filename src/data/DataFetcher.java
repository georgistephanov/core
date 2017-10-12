package data;
import java.io.*;
import java.util.ArrayList;

import java.sql.*;

public class DataFetcher {

	public static ArrayList<String> readFromFile(String fileName, int approxNumOfLines) throws IOException {

		ArrayList<String> file_data = new ArrayList<>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			for (String line = br.readLine(); line != null; line = br.readLine()) {
				file_data.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File '" + fileName + "' not found.");
		}

		return file_data;
	}

	public static void callMySQL() {
		MySQLAccess m = new MySQLAccess();
		try {
			m.readDatabase();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}

	}
}

class MySQLAccess {
	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	public void readDatabase() throws Exception {
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
}