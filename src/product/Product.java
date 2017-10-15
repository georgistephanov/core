package product;

import java.text.DecimalFormat;

public class Product {
	private static long _productID = 123_456_00;
	public long productID;

	private String _name;
	private double _price;
	private int _quantityAvailable, _defaultQuantityAvailable;
	private int _quantityInCart;

	DecimalFormat f = new DecimalFormat("####.##");

	public Product(int id, String n, double p, int q) {
		_name = n;
		_price = p;
		_quantityAvailable = q;
		_defaultQuantityAvailable = q;
		_quantityInCart = 0;
		productID = _productID + id;
	}

	/* ============== GETTERS ============== */
	public int getQuantityAvailable() {
		return _quantityAvailable - _quantityInCart;
	}
	public int getQuantityInCart() { return _quantityInCart; }
	public double getPrice() {
		return (double) Math.round(_price * 100) / 100;
	}
	public String getName() { return _name; }
	public long getID() { return productID; }


	// Subtracts the quantity bought from the initial available quantity
	public void reduceQuantityAvailable() {
		_quantityAvailable--;
	}
	public void reduceQuantityAvailable(int i) {
		_quantityAvailable -= i;
	}
	public void increaseQuantityInCart() {
		_quantityInCart++;
	}
	public void increaseQuantityInCart(int i) {
		_quantityInCart += i;
	}

	public void productRemovedFromCart() {
		_quantityAvailable = _defaultQuantityAvailable;
		_quantityInCart = 0;
	}


	// Prints full product info
	public void printProductInfo() {
		System.out.println("Product ID: " + productID + "\nName: " + _name
				+ "\nPrice: " + f.format(_price) + "\nQuantity: " + _quantityAvailable + "\n");
	}
	// Prints short product info
	public void printShortProductInfo() {
		System.out.println(_quantityInCart + " " + _name + ", $" + f.format(_price));
	}
}
