package mealplaner.commons;

import mealplaner.errorhandling.MealException;

public class NonnegativeInteger {
	public final int value;

	public NonnegativeInteger(int positive) {
		if (positive < 0) {
			throw new MealException("Integer must be nonnegative");
		}
		this.value = positive;
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
