package lib;

import core.Engine;
import data.ProductDatabase;
import data.UserDatabase;
import product.ProductCatalog;
import java.time.Duration;
import java.time.Instant;

public class SystemDiagnostics extends java.util.TimerTask {
	private static SystemDiagnostics _systemDiagnostics;

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

	public static void initialise() {
		if (_systemDiagnostics == null) _systemDiagnostics = new SystemDiagnostics();
		if (_userDatabase == null) _userDatabase = new UserDatabase();
		if (_productDatabase == null) _productDatabase = new ProductDatabase();
	}

	public static SystemDiagnostics getInstance() {	return _systemDiagnostics; }

	// Implements the run() class of TimerTask so that we can map it to a timer object
	public void run() {
		_testSystem();
		_testsPerformed++;
	}

	// Prints the full system status from the manager/admin menu
	public void printSystemInformation() {
		System.out.println("==== System Information ====");
		_printStatuses();
		_printTimeActive();
		_printChecksPerformed();
		System.out.println("============================\n");
	}

	// Controls the flow of the program. If there is something wrong it automatically terminates the application.
	private void _testSystem() {
		_update();

		if (_engineRunning && _databaseRunning && _catalogAvailable) {
			_testsPassed++;
			return;
		} else {

			// Trying to resolve the issues

			if (!_engineRunning) {
				System.out.println("(SYS_DIAGNOSTICS) Engine has stopped running");
			} else if (!_databaseRunning) {
				System.out.println("(SYS_DIAGNOSTICS) No database connection.");
			} else {
				System.out.println("(SYS_DIAGNOSTICS) Catalog is not available");
			}
		}

		Engine.terminateApplication();
	}

	// Updates the control-flow variables
	private void _update() {
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

// TODO: Add run initial test to see if user has been authorised