package product;

public class Product {
	private static long _productID = 123_456_00;
	public long productID;

	private String _name;
	private double _price;
	private int _quantityAvailable;

	public Product(String n, double p, int q) {
		_name = n;
		_price = p;
		_quantityAvailable = q;
		productID = _productID++;
	}

	// GETTERS
	public int getQuantity() {
		return _quantityAvailable;
	}
	public double getPrice() {
		return (double) Math.round(_price * 100) / 100;
	}
	public String getName() { return _name; }


	// Subtracts the quantity bought from the initial available quantity
	public void reduceQuantity() {
		_quantityAvailable--;
	}
	public void reduceQuantity(int i) {
		_quantityAvailable -= i;
	}

	public void printProductInfo() {
		System.out.println("Product ID: " + productID + "\nName: " + _name
				+ "\nPrice: " + _price + "\nQuantity: " + _quantityAvailable + "\n");
	}
	public void printShortProductInfo() {
		System.out.println(_name + ", $" + _price);
	}
}
