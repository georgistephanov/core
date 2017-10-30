package lib;

import core.Engine;
import java.util.InputMismatchException;

public class GeneralHelperFunctions {

	public static boolean askForDecision() {
		char opt = Engine.inputScanner.next().toLowerCase().charAt(0);
		Engine.inputScanner.nextLine();

		if (opt == 'y')
			return true;

		return false;
	}

	public static int inputIntegerOption(int start, int end) {
		int opt;

		try {
			opt = Engine.inputScanner.nextInt();
			Engine.inputScanner.nextLine();

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

	public static void generateMenu(String options[]) {

		if (options != null) {

			System.out.println('\n' + options[0]);

			for (int i = 1; i < options.length - 1; i++) {
				System.out.println("\t" + i + ". " + options[i]);
			}

			System.out.println("\n\t0. " + options[options.length-1]);
		}
	}
}
