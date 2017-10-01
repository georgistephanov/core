package product;

public class Product {
	private static long _productID = 123_456_00;
	public long productID;

	public String name;
	double price;
	int quantityAvailable;

	public Product(String n, double p, int q) {
		name = n;
		price = p;
		quantityAvailable = q;
		productID = _productID++;
	}

	// GETTERS
	public int getQuantity() {
		return quantityAvailable;
	}
	public double getPrice() {
		return (double) Math.round(price * 100) / 100;
	}
	public String getName() { return name; }



	public void reduceQuantity() {
		quantityAvailable--;
	}

	public void reduceQuantity(int i) {
		quantityAvailable -= i;
	}

	public void printProductInfo() {
		System.out.println("Product ID: " + productID + "\nName: " + name
				+ "\nPrice: " + price + "\nQuantity: " + quantityAvailable + "\n");
	}
}
