// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.ZERO;
import static mealplaner.model.recipes.Ingredient.emptyIngredient;

import mealplaner.commons.NonnegativeFraction;

public final class QuantitativeIngredient {
  public static final QuantitativeIngredient DEFAULT = new QuantitativeIngredient(
      emptyIngredient(), ZERO);

  private final Ingredient ingredient;
  private final NonnegativeFraction amount;

  private QuantitativeIngredient(Ingredient ingredient,
      NonnegativeFraction amount) {
    this.ingredient = ingredient;
    this.amount = amount;
  }

  public static QuantitativeIngredient create(Ingredient ingredient, NonnegativeFraction amount) {
    return new QuantitativeIngredient(ingredient, amount);
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public NonnegativeFraction getAmount() {
    return amount;
  }

  @Override
  public String toString() {
    return "[ " + ingredient.toString() + ", "
        + amount.toString() + "]";
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((amount == null) ? 0 : amount.hashCode());
    result = 31 * result + ((ingredient == null) ? 0 : ingredient.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    QuantitativeIngredient other = (QuantitativeIngredient) obj;
    return ingredient.equals(other.ingredient)
        && amount.equals(other.amount);
  }
}
