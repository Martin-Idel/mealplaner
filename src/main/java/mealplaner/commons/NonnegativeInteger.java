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

	@Override
	public int hashCode() {
		return 31 + value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		NonnegativeInteger other = (NonnegativeInteger) obj;
		return value == other.value;
	}
}
