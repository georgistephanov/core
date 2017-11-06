package lib.payment;

public interface Card {
	boolean makePayment(double amount);
	double getBalance();
	String toString();
}
