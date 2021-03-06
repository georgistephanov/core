package lib.payment;

import data.Database;
import data.UserDatabase;

public final class VisaCard implements Card {

	private double balance;
	private String cardNumber;
	private boolean cardActive;

	private UserDatabase userDatabase;


	public VisaCard(int user_id) {
		userDatabase = new UserDatabase();

		cardNumber = userDatabase.getCardNumber(user_id);
		balance = userDatabase.getCardBalance(user_id);

		cardActive = (cardNumber != null && balance != -1);
	}

	public boolean makePayment(double amount) {
		if (balance >= amount) {
			if (userDatabase.updateCardBalance(cardNumber, balance - amount)) {
				balance -= amount;
				return true;
			} else {
				System.out.println("There was an error while trying to connect to the database."
								+ "\nThe payment was unsuccessful. Please try again in a few moments!\n");
			}
		} else {
			System.out.println("Payment unsuccessful!\nInsufficient amount of money in your card!");
		}

		return false;
	}

	@Override public String toString() {
		return "\tCard #: " + _getCardNumber()
			+ "\n\tBalance: $" + getBalance();
	}

	public double getBalance() {
		return (double) Math.round(balance * 100) / 100;
	}
	private String _getCardNumber() {
		return cardNumber.substring(0, 4) + "-" + cardNumber.substring(4, 8) + "-"
				+ cardNumber.substring(8, 12) + "-" + cardNumber.substring(12, 16);
	}
	public boolean isCardActive() {
		return cardActive;
	}
}
