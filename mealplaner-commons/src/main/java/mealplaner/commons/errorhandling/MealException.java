// SPDX-License-Identifier: MIT

package mealplaner.commons.errorhandling;

public class MealException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public MealException(String s, Exception ex) {
    super(s, ex);
  }

  public MealException(String s) {
    super(s);
  }
}