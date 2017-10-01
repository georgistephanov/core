package user;
import product.*;
import java.util.ArrayList;

public class Cart {

	ArrayList<Product> items;
	double totalAmount;

	Cart() {
		items = new ArrayList<>();
		totalAmount = 0;
	}

	public void addToCart(Product p) {
		items.add(p);
		totalAmount += p.getPrice();
	}

	public double getTotalAmount() {
		return totalAmount;
	}

}