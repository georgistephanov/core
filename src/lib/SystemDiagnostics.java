package lib;

import core.Engine;
import data.MySQLAccess;
import product.ProductCatalog;

public class SystemDiagnostics {
	private static SystemDiagnostics _systemDiagnostics;

	private int _timeRunning;
	private boolean _engineRunning;
	private boolean _databaseRunning;
	private boolean _catalogAvailable;

	private SystemDiagnostics() {
		_timeRunning = 1;	//TODO: Detect this correctly
	}

	public static SystemDiagnostics getInstance() {
		if (_systemDiagnostics == null) {
			_systemDiagnostics = new SystemDiagnostics();
		}

		return _systemDiagnostics;
	}

	// Controls the flow of the program. If there is something wrong it automatically terminates the application.
	public void testSystem() {
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

	private void _update() {
		_engineRunning = Engine.getInstance().isRunning();
		_databaseRunning = MySQLAccess.getMySQLObject().isRunning();
		_catalogAvailable = ProductCatalog.isAvailable();
	}
}
