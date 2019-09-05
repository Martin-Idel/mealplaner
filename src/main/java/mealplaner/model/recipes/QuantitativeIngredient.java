// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.NonnegativeFraction.ZERO;
import static mealplaner.model.recipes.Ingredient.emptyIngredient;

import java.security.InvalidParameterException;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;

public final class QuantitativeIngredient {
  public static final QuantitativeIngredient DEFAULT = new QuantitativeIngredient(
      emptyIngredient(), Measure.NONE, ZERO);

  private final Ingredient ingredient;
  private final Measure measure;
  private final NonnegativeFraction amount;

  private QuantitativeIngredient(
      Ingredient ingredient, Measure measure, NonnegativeFraction amount) {
    if (!ingredient.getMeasures().contains(measure)) {
      throw new InvalidParameterException("Measure must be a valid measure for Ingredients");
    }
    this.ingredient = ingredient;
    this.measure = measure;
    this.amount = amount;
  }

  public static QuantitativeIngredient createQuantitativeIngredient(
      Ingredient ingredient, Measure measure, NonnegativeFraction amount) {
    return new QuantitativeIngredient(ingredient, measure, amount);
  }

  public static QuantitativeIngredient createQuantitativeIngredient(
      Ingredient ingredient, NonnegativeFraction amount) {
    return new QuantitativeIngredient(ingredient, ingredient.getPrimaryMeasure(), amount);
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public NonnegativeFraction getAmount() {
    return amount;
  }

  public Measure getMeasure() {
    return measure;
  }

  public QuantitativeIngredient multiplyBy(NonnegativeInteger integer) {
    return new QuantitativeIngredient(this.ingredient, measure, amount.multiplyBy(integer));
  }

  public QuantitativeIngredient divideBy(NonnegativeInteger integer) {
    return new QuantitativeIngredient(this.ingredient, measure, amount.divideBy(integer));
  }

  public QuantitativeIngredient convertToPrimaryMeasure() {
    return convertToMeasure(ingredient.getPrimaryMeasure());
  }

  public QuantitativeIngredient convertToMeasure(Measure measure) {
    var convertedAmount = ingredient.getMeasures()
        .getConversionFactor(this.measure, measure);
    return new QuantitativeIngredient(this.ingredient, measure, this.amount.multiplyBy(convertedAmount));
  }

  @Override
  public String toString() {
    return "[ " + ingredient.toString() + ", "
        + measure.toString() + ", "
        + amount.toString() + "]";
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + ((amount == null) ? 0 : amount.hashCode());
    result = 31 * result + ((measure == null) ? 0 : measure.hashCode());
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
        && measure.equals(other.measure)
        && amount.equals(other.amount);
  }

}
