package core;

public class Main {

	public static void main(String[] args) {

		Engine e = Engine.getInstance();
		e.execute();

	}
}


// TODO: create a logger for all the errors to be logged in during the current session and save them to a file

/* TODO: Use the factory method design pattern combined with dependency inversion to
   TODO: add categories to the products and create the correct object dependant on the category

   TODO: See if you can somehow use Abstract Factory to manage different product attributes depending
   TODO: on the category.
*/