package lib;

import java.util.Scanner;

public class GeneralHelperFunctions {
	public static boolean askForDecision() {
		Scanner s = new Scanner(System.in);
		char opt = s.nextLine().toLowerCase().charAt(0);

		if (opt == 'y')
			return true;

		return false;
	}
}
