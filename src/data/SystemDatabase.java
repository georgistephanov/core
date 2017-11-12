package data;

import core.Engine;
import lib.GeneralHelperFunctions;

public class SystemDatabase extends Database {
	public SystemDatabase() {
		// The first parameter is the database to be used
		// The second parameter is the filename which will be used by the logger
		super("core_system", "SystemDatabase");
	}

	public int getTimerRate() {
		// Default rate of 10 seconds
		int rate = 10 * 1000;

		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT time_between_tests FROM variables");

			if (resultSet.next()) {
				int rateFromDatabase = resultSet.getInt("time_between_tests");

				if (rateFromDatabase > 0) {
					rate = rateFromDatabase * 1000;
				} else {
					rate = -1;
				}
			}
		}
		catch (Exception e) {
			logError(e, "getTImerRate");
		}
		finally {
			_close();
		}

		return rate;
	}
	public boolean printSystemVariablesAsMenu() {
		try {
			connect = _prepareConnection();
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT time_between_tests FROM variables");

			if (resultSet.next()) {
				int time = resultSet.getInt("time_between_tests");
				String menu[] = {"System variables:", "System tests time rate:\t" + time + " seconds", "Back"};
				GeneralHelperFunctions.generateMenu(menu);

				return true;
			}
		}
		catch (Exception e) {
			logError(e, "printSystemVariablesAsMenu");
		}
		finally {
			_close();
		}

		return false;
	}
	public void changeTestTimeRate() {
		System.out.println("Enter timer rate in seconds: ");
		int seconds = Engine.getInputScanner().nextInt();
		Engine.flushInputScanner();

		try {
			connect = _prepareConnection();
			preparedStatement = connect.prepareStatement("UPDATE variables SET time_between_tests=?");
			preparedStatement.setInt(1, seconds);
			preparedStatement.executeUpdate();

			System.out.println("Successfully changed the time between the system background tests."
							+ "\nRestart the application in order for your changes to become active.");
		}
		catch (Exception e) {
			logError(e, "changeTestTimeRate");
		}
		finally {
			_close();
		}
	}
}
