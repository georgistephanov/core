package lib;

import core.Engine;
import data.ProductDatabase;
import data.UserDatabase;
import product.ProductCatalog;
import java.time.Duration;
import java.time.Instant;


// TODO: Make this class use the Observer pattern and listen for
// TODO: updates from the Engine mainly so that if some crucial
// TODO: component changes it should run some tests to check
// TODO: whether the system is okay to continue running.

// TODO: Add counter how many times the program has been executed correctly and how many by an error
// TODO: Count the total tests performed ever + the total tests passed/failed
// TODO: Add option for the admin to change the frequency of the tests

public class SystemDiagnostics extends java.util.TimerTask implements lib.observer.Observer {
	private static SystemDiagnostics _systemDiagnostics;

	private boolean _userAuthorised;
	private boolean _engineRunning;
	private boolean _databaseRunning;
	private boolean _catalogAvailable;

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
	}

	public static SystemDiagnostics getInstance() {
		if (_systemDiagnostics == null) {
			initialise();
		}

		return _systemDiagnostics;
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

	/* Runs the needed tests after the login prompt
	public void runStartupTest() {
		if ( !(Engine.getInstance().isUserAuthorised()) ) {
			System.out.println("(SYS_DIAGNOSTICS) No authorised user");
			Engine.getInstance().terminateApplication();
		}

		_testSystem();
	}*/

	// Prints the full system status from the manager/admin menu
	public void printSystemInformation() {
		System.out.println("==== System Information ====");
		_printStatuses();
		_printTimeActive();
		_printChecksPerformed();
		System.out.println("============================\n");
	}

	// Returns whether there is a logged in user in the application so that it could be used on termination
	public boolean isUserAuthorised() {
		return _userAuthorised;
	}

	// Controls the flow of the program. If there is something wrong it automatically terminates the application.
	private void _testSystem() {
		_testsPerformed++;
		_updateStatusVariables();

		if (_engineRunning && _databaseRunning && _catalogAvailable) {
			_testsPassed++;
			return;
		} else {

			// TODO: Try to resolve the issues

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
}