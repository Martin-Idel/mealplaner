package mealplaner.commons;

import static java.lang.Integer.compare;

import mealplaner.commons.errorhandling.MealException;

public class NonnegativeInteger implements Comparable<NonnegativeInteger> {
	public static final NonnegativeInteger ZERO = new NonnegativeInteger(0);
	public static final NonnegativeInteger ONE = new NonnegativeInteger(1);
	public static final NonnegativeInteger TWO = new NonnegativeInteger(2);
	public static final NonnegativeInteger THREE = new NonnegativeInteger(3);
	public static final NonnegativeInteger FOUR = new NonnegativeInteger(4);

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

	public NonnegativeInteger multiplyBy(NonnegativeInteger multiplier) {
		return nonNegative(value * multiplier.value);
	}

	public NonnegativeInteger divideBy(NonnegativeInteger divisor) {
		return nonNegative((int) ((float) value / divisor.value));
	}

	public NonnegativeInteger add(NonnegativeInteger summand) {
		return nonNegative(value + summand.value);
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

	@Override
	public int compareTo(NonnegativeInteger nonnegative) {
		return compare(value, nonnegative.value);
	}
}
