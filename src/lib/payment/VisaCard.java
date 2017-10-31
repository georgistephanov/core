package lib.payment;

import core.Engine;
import data.MySQLAccess;

public final class VisaCard implements Card {

	private double balance;
	private String cardNumber;
	private boolean cardActive;

	public VisaCard(int user_id) {
		cardNumber = MySQLAccess.getMySQLObject().getCardNumber(user_id);
		balance = MySQLAccess.getMySQLObject().getCardBalance(user_id);

		if (cardNumber == null || balance == -1) {
			cardActive = false;
		} else {
			cardActive = true;
		}
	}

	public VisaCard(int user_id, String cardNumber, double balance) {
		this.cardNumber = cardNumber;
		this.balance = balance;
	}

	public boolean makePayment(double amount) {
		if (balance >= amount) {
			if (MySQLAccess.getMySQLObject().updateCardBalance(cardNumber, balance - amount)) {
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


	public void printFullInformation() {
		System.out.println("\tCard #: " + _getCardNumber()
			+ "\n\tBalance: $" + getBalance());
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
