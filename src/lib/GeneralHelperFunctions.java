package lib;

import core.Engine;

import java.text.SimpleDateFormat;
import java.util.InputMismatchException;

public class GeneralHelperFunctions {

	public static boolean askForDecision() {
		char opt = Engine.getInputScanner().next().toLowerCase().charAt(0);
		Engine.getInputScanner().nextLine();

		if (opt == 'y')
			return true;

		return false;
	}

	public static int inputIntegerOption(int start, int end) {
		int opt;

		try {
			opt = Engine.getInputScanner().nextInt();
			Engine.getInputScanner().nextLine();

			if (opt >= start && opt <= end)
				return opt;
			else
				return -1;
		}
		catch (InputMismatchException e) {
			System.out.println("Please provide a correct input.\nWant to continue? (y/n)");

			if (askForDecision()) {
				return inputIntegerOption(start, end);
			} else {
				return -1;
			}
		}
	}

	/**
	 * This method generates a menu in the format explained below. It is used to print
	 * a menu from array of options and then be used together with inputIntegerOption
	 * and a switch case to gain the whole menu functionality. It doesn't run checks
	 * whether a correct digit is being given at the beginning of the string, as it
	 * would be incompatible for the client to give already existing digit because
	 * the switch case then will fail.
	 *
	 * @param options : array of strings in the format: the first element in the array
	 *                  is the menu name, the last element of the array is either "Back"
	 *                  or "Exit", depending on the level of nested menus, and anything
	 *                  in between is a menu option printed with the corresponding select
	 *                  number before it in the format "x. Option" where x stands for the
	 *                  array index number and Option stands for the String with that index.
	 *                  If the String begins with a digit, that digit stands for the option
	 *                  number.
	 */
	public static void generateMenu(String options[]) {
		boolean hasExplicitOption = false;

		if (options != null) {

			System.out.println('\n' + options[0]);

			for (int i = 1; i < options.length - 1; i++) {
				// If the option starts with a number, this should be the selection number in the menu
				if (Character.isDigit(options[i].charAt(0))) {
					System.out.println("\n\t" + options[i].charAt(0) + ". " + options[i].substring(1));
					hasExplicitOption = true;
				} else {
					System.out.println("\t" + i + ". " + options[i]);
				}
			}

			if (hasExplicitOption) {
				System.out.println("\t0. " + options[options.length-1]);
			} else {
				System.out.println("\n\t0. " + options[options.length - 1]);
			}
		}
	}

	/**
	 * This message takes either one or multiple strings and prints them
	 * between two rows of equal signs (====) with the correct length.
	 */
	public static void printBlockMessage(String... message) {
		if (message != null && message.length > 0) {

			// Getting the longest line length, which will be the length of the block borders
			int longestLineLength = -1;
			for (String m : message) {
				if (m.length() > longestLineLength)
					longestLineLength = m.length();
			}

			// Generating the correct amount of equal signs which would enclose the message
			StringBuilder blockLine = new StringBuilder();
			for (int i = 0; i < longestLineLength; i++) {
				blockLine.append("=");
			}

			// Printing the block message
			System.out.println("\n" + blockLine.toString());
			for (String m : message) {
				if (m.length() > 0) {
					System.out.println(m);
				}
			}
			System.out.println(blockLine.toString() + "\n");
		}
	}

	/**
	 * Returns formatted time out of seconds in the format
	 * hh:mm:ss.
	 * @param seconds
	 * @return String with the formatted time
	 */
	public static String getFormattedTimeFromSeconds(int totalSeconds) {
		int hours = totalSeconds / 3600;
		int minutes = (totalSeconds % 3600) / 60;
		int seconds = totalSeconds % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
