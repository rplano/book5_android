package variationenzumthema_st2;

import acm_graphics.ConsoleProgram;

/**
 * MIT License (http://choosealicense.com/licenses/mit/)
 * 
 * HelloWorld
 * 
 * A simple ConsoleProgram.
 * 
 * @see http://www.VariationenZumThema.de/
 * @author Ralph P. Lano
 */
public class HelloWorld extends ConsoleProgram {

	public void run() {
		println("Hello World!");
		String name = readLine("Enter name: ");
		println("Hello " + name + "!");
		int age = readInt("Enter age: ");
		println("Your are " + age + " years old!");
	}
}