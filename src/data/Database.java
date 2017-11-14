package data;

import lib.Logger;
import java.sql.*;


public abstract class Database {
	/* =============== JDBC Variables =============== */
	Connection connect = null;
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;

	private String databaseToUse;
	private String fileName;
	private Logger logger = Logger.getInstance();


	/* =============== Constructor =============== */
	Database(String databaseToUse, String filename) { this.databaseToUse = databaseToUse; this.fileName = filename; }

	/* =============== Connection Generating Method =============== */
	Connection _prepareConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/" + databaseToUse + "?autoReconnect=true&useSSL=false", "root", "");
	}

	/* =============== Helper Methods =============== */
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
	void logError(Exception e, String methodName) {
		logger.logError(e, fileName, methodName);
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