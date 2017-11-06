package product;

import java.text.DecimalFormat;


/*
	The Builder pattern has been used instead of constructors as class'
	data fields might be well expanded and it would be more convenient
	to have the Builder's advantages upfront.
 */
public class Product {
	private static final long PRODUCT_ID = 123_456_00;
	private long _productID;

	private String _name;
	private double _price;
	private int _quantityAvailable, _defaultQuantityAvailable;
	private int _quantityInCart = 0;

	private String _description;

	private DecimalFormat f = new DecimalFormat("####.##");

	private Product(Builder builder) {
		_name = builder.name;
		_price = builder.price;
		_quantityAvailable = builder.quantityAvailable;
		_defaultQuantityAvailable = builder.quantityAvailable;
		_productID = PRODUCT_ID + builder.productID;

		_description = builder.description;
	}

	public static class Builder {
		// Required parameters
		private int productID;
		private String name;
		private double price;
		private int quantityAvailable;

		// Optional parameters
		private String description = "";

		public Builder(int productID, String name, double price, int quantityAvailable) {
			this.productID = productID;
			this.name = name;
			this.price = price;
			this.quantityAvailable = quantityAvailable;
		}

		// The setter methods return Builder object so that multiple
		// setter method calls could be chained to avoid verbosity
		public Builder description(String description) {
			this.description = description;
			return this;
		}

		public Product build() {
			return new Product(this);
		}
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
	public long getID() { return _productID; }
	public int getBaseID() { return (int) _productID - 123_456_00; }


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


	public String toString() {
		return "\nProduct ID: " + _productID
				+ "\nName: " + _name
				+ "\nPrice: " + f.format(_price)
				+ "\nQuantity: " + _quantityAvailable;
	}
}
