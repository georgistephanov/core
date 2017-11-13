package lib;

import core.Engine;
import data.*;
import product.ProductCatalog;
import java.time.Duration;
import java.time.Instant;

public class SystemDiagnostics extends java.util.TimerTask implements lib.observer.Observer {
	private static SystemDiagnostics _systemDiagnostics;

	private boolean _userAuthorised;
	private boolean _engineRunning;
	private boolean _databaseRunning;
	private boolean _catalogAvailable;

	// Timer variable which executes the tests periodically
	private java.util.Timer timer;

	// Holding instances of both databases so that they aren't constructed on every update
	private static UserDatabase _userDatabase;
	private static ProductDatabase _productDatabase;

	// In-class information data
	private int _testsPerformed = 0;
	private int _testsPassed = 0;
	private Instant _timestamp = Instant.now();

	private SystemDiagnostics() { }

	private static void initialise() {
		if (_systemDiagnostics == null) _systemDiagnostics = new SystemDiagnostics();
		if (_userDatabase == null) _userDatabase = new UserDatabase();
		if (_productDatabase == null) _productDatabase = new ProductDatabase();

		// Updates the counter which registers how many times the program has been run
		new SystemDatabase().registerProgramStart();

		// This performs the cleanup operations first thing when the program is run
		_performDatabaseCleanup();
	}

	public static SystemDiagnostics getInstance() {
		if (_systemDiagnostics == null) {
			initialise();
		}

		return _systemDiagnostics;
	}


	// Methods to provide accessing points for the TimerTask to execute the updates
	public void startTimer() {
		int timerRate = new SystemDatabase().getTimerRate();

		if (timerRate > 0) {
			timer = new java.util.Timer();
			timer.scheduleAtFixedRate(_systemDiagnostics, 0, new SystemDatabase().getTimerRate());
		} else {
			// Do not run the timer
		}
	}
	public void stopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}
	// Implements the run() method of TimerTask so that we can map it to a timer object
	public void run() {
		_testSystem();
	}

	// Implements the update() method of the Observer interface
	public void update() {
		_userAuthorised = Engine.getInstance().isUserAuthorised();
		_testSystem();
	}

	// Prints the full system status from the manager/admin menu
	public void printSystemInformation() {
		System.out.println("==== System Information ====");
		_printStatuses();
		_printTimeActive();
		_printChecksPerformed();
		new SystemDatabase().printTotalTestsPerformed();
		System.out.println("============================\n");
	}

	// Returns whether there is a logged in user in the application so that it could be used on termination
	public boolean isUserAuthorised() {
		return _userAuthorised;
	}

	//Returns the total tests performed and passed
	public int getTestsPerformed() { return _testsPerformed; }
	public int getTestsPassed() { return _testsPassed; }


	// Controls the flow of the program. If there is something wrong it automatically terminates the application.
	private void _testSystem() {
		_testsPerformed++;
		_updateStatusVariables();

		if (_engineRunning && _databaseRunning && _catalogAvailable) {
			_testsPassed++;
			return;
		} else {

			if (!_engineRunning) {
				System.out.println("(SYS_DIAGNOSTICS) Engine has stopped running");
			} else if (!_databaseRunning) {
				System.out.println("(SYS_DIAGNOSTICS) No database connection.");
			} else {
				System.out.println("(SYS_DIAGNOSTICS) Catalog is not available");
			}
		}

		Engine.getInstance().terminateApplication();
	}

	// Updates the control-flow variables
	private void _updateStatusVariables() {
		_engineRunning = Engine.getInstance().isRunning();
		_catalogAvailable = ProductCatalog.isAvailable();
		_databaseRunning = _checkDatabases();
	}

	// Checks whether all databases are running
	private boolean _checkDatabases() {
		return _userDatabase.isRunning() && _productDatabase.isRunning();
	}

	// Prints the components and their statuses
	private void _printStatuses() {
		System.out.println("Engine: " + (_engineRunning ? "running" : "not running"));
		System.out.println("Database: " + (_databaseRunning ? "running" : "not running"));
		System.out.println("Catalog: " + (_catalogAvailable ? "available" : "not available"));
		System.out.println();
	}
	// Prints the total time the program has been running
	private void _printTimeActive() {
		Duration duration = Duration.between(_timestamp, Instant.now());
		long totalSeconds = duration.getSeconds();
		int hours = (int) totalSeconds / 3600;
		int minutes = (int) (totalSeconds % 3600) / 60;
		int seconds = (int) totalSeconds % 60;

		System.out.printf("Time active: %02d:%02d:%02d\n\n", hours, minutes, seconds);
	}
	// Prints the total amount of checks performed
	private void _printChecksPerformed() {
		System.out.println("System tests performed: " + _testsPerformed);
		System.out.println("System tests passed: " + _testsPassed);
	}
	// Performs database cleanup when the application is started
	private static void _performDatabaseCleanup() {
		// Clean the empty sessions
		_userDatabase.deleteInvalidUserSessions();
	}
}