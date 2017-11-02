package lib;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*  An application-wide global logger to take care of
 *  exceptions and properly log them to a file, so they
 *  can be reviewed later on.
 */
public final class Logger {
	private static Logger logger = new Logger();

	// TODO: Fix this as it doesn't write to the file
	private static final String LOG_FILE_PATH = "../../log.txt";

	private Logger() {}
	public static Logger getInstance() { return logger; }

	// The logger method which handles all exceptions
	public void logError(Exception e, String fileName, String methodName) {
		String errorMessage = _generateErrorMessage(e, fileName, methodName);

		_logErrorToFile(errorMessage);

		System.err.println(errorMessage);
	}

	/*  Method which transforms the error to a full error message
	 *  with timestamp and file and method names in which the
	 *  exception has occurred. It takes the exception itself,
	 *  the file name and the method name in which it occurred
	 *  as method parameters and returns a String with the
	 *  error message produced from them.
	 */
	private String _generateErrorMessage(Exception e, String fileName, String methodName) {
		String errorMessage = "";

		// Generate the date and time of the occurrence of the exception
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		errorMessage += dateFormat.format(date);

		// Add the file and method names in which the exception occurred
		errorMessage += "  (" + fileName + " : " + methodName + "())  ";

		// Add the full error message of the exception
		errorMessage += e.toString() + "\n";

		return errorMessage;
	}

	/*  Method which logs the generated error message to a local
	 *  file on the system. It takes a string with the full error
	 *  message which is to be logged on the file as a parameter.
	 */
	private void _logErrorToFile(String errorMessage) {
		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(LOG_FILE_PATH, true);
			fileWriter.write(errorMessage);
		}
		catch (IOException e) {
			System.err.println("IOException when trying to write to the log file.");
		}
		finally {
			try {
				if (fileWriter != null) {
					fileWriter.close();
				}
			}
			catch (IOException e) {
				System.err.println("IOException when trying to close the FileWriter");
			}
		}
	}

}
