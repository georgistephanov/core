package core;

public class Main {

	public static void main(String[] args) {

		Engine e = Engine.getInstance();
		e.execute();

	}
}


/* TODO: Use the factory method design pattern combined with dependency inversion to
   TODO: add categories to the products and create the correct object dependant on the category
   TODO: and also use it to create different types of accounts (Basic, Premium, Employee, Manager, Owner)

   TODO: See if you can somehow use Abstract Factory to manage different product attributes depending
   TODO: on the category.
*/

// TODO: Store purchase information about the user in the database
// TODO: Add logout and change user option

// GENERAL TODO: Look into Java best practices if it is the best decision to have that much static classes.