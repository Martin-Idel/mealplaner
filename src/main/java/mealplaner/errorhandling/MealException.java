package mealplaner.errorhandling;

public class MealException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MealException(String s) {
		super(s);
	}
}