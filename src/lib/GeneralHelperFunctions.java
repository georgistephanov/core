package lib;

import core.Engine;
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
}
