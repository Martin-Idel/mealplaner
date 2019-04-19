// SPDX-License-Identifier: MIT

package mealplaner.commons;

import static java.lang.Integer.compare;

import mealplaner.commons.errorhandling.MealException;

public final class NonnegativeInteger implements Comparable<NonnegativeInteger> {
  public static final NonnegativeInteger ZERO = new NonnegativeInteger(0);
  public static final NonnegativeInteger ONE = new NonnegativeInteger(1);
  public static final NonnegativeInteger TWO = new NonnegativeInteger(2);
  public static final NonnegativeInteger THREE = new NonnegativeInteger(3);
  public static final NonnegativeInteger FOUR = new NonnegativeInteger(4);
  public static final NonnegativeInteger FIVE = new NonnegativeInteger(5);
  public static final NonnegativeInteger SIX = new NonnegativeInteger(6);
  public static final NonnegativeInteger SEVEN = new NonnegativeInteger(7);

  public final int value;

  private NonnegativeInteger(int nonnegative) {
    this.value = nonnegative;
  }

  public static NonnegativeInteger nonNegative(int nonnegative) {
    if (nonnegative < 0) {
      throw new MealException("Integer must be nonnegative");
    }
    return new NonnegativeInteger(nonnegative);
  }

  private static NonnegativeInteger uncheckedNonNegative(int nonnegative) {
    return new NonnegativeInteger(nonnegative);
  }

  public NonnegativeInteger multiplyBy(NonnegativeInteger multiplier) {
    return uncheckedNonNegative(value * multiplier.value);
  }

  public NonnegativeInteger divideBy(NonnegativeInteger divisor) {
    return uncheckedNonNegative((int) ((float) value / divisor.value));
  }

  public NonnegativeInteger add(NonnegativeInteger summand) {
    return uncheckedNonNegative(value + summand.value);
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
