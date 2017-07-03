package mealplaner.errorhandling;

/**
 * Martin Idel, MealException: base class for errors specific to this
 * application. Can be thrown from classes 'Gericht' and 'Setting'.
 **/

public class MealException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MealException() {
	}

	public MealException(String s) {
		super(s);
	}

}