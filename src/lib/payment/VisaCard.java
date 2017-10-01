package lib.payment;

public class VisaCard implements Card {

	double balance;

	public VisaCard() {
		balance = 2000.0;
	}

	public boolean makePayment(double amount) {
		if (balance >= amount) {
			balance -= amount;
			return true;
		}
		else
			return false;
	}

	public double getBalance() {
		// rounds to 2 decimal places
		return (double) Math.round(balance * 100) / 100;
	}
}
