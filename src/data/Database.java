package data;

import lib.Logger;
import java.util.ArrayList;
import java.sql.*;


public abstract class Database {
	Connection connect = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private String databaseToUse;
	private String fileName; //TODO: keep the name of the file here to avoid writing it on every log

	Database(String databaseToUse) { this.databaseToUse = databaseToUse; }

	Logger logger = Logger.getInstance();


	ArrayList<String> _createStringArrayListFromResultSet(ResultSet resultSet, String columnLabel) throws SQLException {
		ArrayList<String> arrayList = new ArrayList<>();

		while (resultSet.next()) {
			arrayList.add(resultSet.getString(columnLabel));
		}

		return arrayList;
	}

	public boolean isRunning() {
		try {
			_prepareConnection();

			return true;
		}
		catch (Exception e) {
			logger.logError(e, "Database", "isRunning");
		}
		finally {
			_close();
		}

		return false;
	}

	Connection _prepareConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/" + databaseToUse + "?autoReconnect=true&useSSL=false", "root", "");
	}

	void _close() {
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
			logger.logError(e, "Database", "_close");
		}
	}
}