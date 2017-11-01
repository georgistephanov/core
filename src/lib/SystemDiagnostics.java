package lib;

import core.Engine;
import data.MySQLAccess;
import product.ProductCatalog;

import java.time.Duration;
import java.time.Instant;


public class SystemDiagnostics extends java.util.TimerTask {
	private static SystemDiagnostics _systemDiagnostics;

	private boolean _engineRunning;
	private boolean _databaseRunning;
	private boolean _catalogAvailable;

	// In-class information data
	private int _testsPerformed = 0;
	private Instant _timestamp = Instant.now();

	private SystemDiagnostics() { }

	public static void initialise() { if (_systemDiagnostics == null) _systemDiagnostics = new SystemDiagnostics(); }

	public static SystemDiagnostics getInstance() {	return _systemDiagnostics; }

	// Implements the run() class of TimerTask so that we can map it to a timer object
	public void run() {
		_testSystem();
		_testsPerformed++;
	}

	// Controls the flow of the program. If there is something wrong it automatically terminates the application.
	private void _testSystem() {
		_update();

		if (_engineRunning && _databaseRunning && _catalogAvailable) {
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
		_databaseRunning = MySQLAccess.getMySQLObject().isRunning();
		_catalogAvailable = ProductCatalog.isAvailable();
	}

	// Prints the total time the program has been running
	private void _printTimeActive() {
		Duration duration = Duration.between(_timestamp, Instant.now());
		long totalSeconds = duration.getSeconds();
		int hours = (int) totalSeconds / 3600;
		int minutes = (int) (totalSeconds % 3600) / 60;
		int seconds = (int) totalSeconds % 60;

		System.out.printf("Time active: %02d:%02d:%02d", hours, minutes, seconds);
	}
}
