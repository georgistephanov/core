package lib;

import java.util.InputMismatchException;
import java.util.Scanner;

public class GeneralHelperFunctions {
	public static boolean askForDecision() {
		Scanner s = new Scanner(System.in);
		char opt = s.nextLine().toLowerCase().charAt(0);

		if (opt == 'y')
			return true;

		return false;
	}

	public static int inputIntegerOption(int start, int end) {
		Scanner s = new Scanner(System.in);
		int opt;

		try {
			opt = s.nextInt();

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
}
