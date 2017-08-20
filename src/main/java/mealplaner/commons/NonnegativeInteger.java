package mealplaner.commons;

import mealplaner.errorhandling.MealException;

public class NonnegativeInteger {
	public final int value;

	public NonnegativeInteger(int nonnegative) {
		if (nonnegative < 0) {
			throw new MealException("Integer must be nonnegative");
		}
		this.value = nonnegative;
	}

	public static NonnegativeInteger nonNegative(int nonnegative) {
		return new NonnegativeInteger(nonnegative);
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
