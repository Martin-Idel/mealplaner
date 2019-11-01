// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;

public final class Recipe {
  private final NonnegativeInteger numberOfPortions;
  private final List<QuantitativeIngredient> ingredients;

  private Recipe(NonnegativeInteger numberOfPortions, List<QuantitativeIngredient> ingredients) {
    this.numberOfPortions = numberOfPortions;
    this.ingredients = ingredients;
  }

  public static Recipe from(NonnegativeInteger numberOfPortions, List<QuantitativeIngredient> ingredients) {
    return new Recipe(numberOfPortions, ingredients);
  }

  public static Recipe createRecipe() {
    return new Recipe(ONE, new ArrayList<>());
  }

  public NonnegativeInteger getNumberOfPortions() {
    return numberOfPortions;
  }

  public List<QuantitativeIngredient> getIngredientsWithPrimaryMeasureFor(
      final NonnegativeInteger numberOfPeople) {
    Map<Ingredient, NonnegativeFraction> combinedIngredients = new HashMap<>();
    for (var quantitativeIngredient : ingredients) {
      var convertedQuantitativeIngredient = quantitativeIngredient
          .multiplyBy(numberOfPeople)
          .divideBy(numberOfPortions)
          .convertToPrimaryMeasure();
      combinedIngredients.compute(convertedQuantitativeIngredient.getIngredient(),
          (ingredient, oldValue) ->
              (oldValue == null)
                  ? convertedQuantitativeIngredient.getAmount()
                  : oldValue.add(convertedQuantitativeIngredient.getAmount()));
    }
    return combinedIngredients.entrySet().stream()
        .map(entry -> createQuantitativeIngredient(
            entry.getKey(),
            entry.getValue()))
        .collect(Collectors.toUnmodifiableList());
  }

  public List<QuantitativeIngredient> getIngredientsAsIs() {
    return ingredients;
  }

  public List<QuantitativeIngredient> getIngredientListFor(
      final NonnegativeInteger numberOfPeople) {
    return getIngredientListWithMultipliedAmount(value -> value
        .multiplyBy(numberOfPeople)
        .divideBy(numberOfPortions));
  }

  public List<QuantitativeIngredient> getIngredientListAsIs() {
    return getIngredientListWithMultipliedAmount(Function.identity());
  }

  private List<QuantitativeIngredient> getIngredientListWithMultipliedAmount(
      Function<NonnegativeFraction, NonnegativeFraction> mapValues) {
    return ingredients.stream()
        .map(ingredient -> createQuantitativeIngredient(
            ingredient.getIngredient(),
            ingredient.getMeasure(),
            mapValues.apply(ingredient.getAmount())))
        .collect(toList());
  }

  @Override
  public String toString() {
    return "Recipe [numberOfPortions=" + numberOfPortions + ", ingredients=" + ingredients
        + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (ingredients == null ? 0 : ingredients.hashCode());
    result = prime * result + numberOfPortions.value;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Recipe other = (Recipe) obj;
    return ingredients.equals(other.ingredients)
        && numberOfPortions.equals(other.numberOfPortions);
  }
}
