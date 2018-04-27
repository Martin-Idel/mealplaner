// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeFraction.ZERO;
import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.recipes.QuantitativeIngredient.create;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;

public final class Recipe {
  private final NonnegativeInteger numberOfPortions;
  private final Map<Ingredient, NonnegativeFraction> ingredients;

  private Recipe(NonnegativeInteger numberOfPortions,
      Map<Ingredient, NonnegativeFraction> ingredients) {
    this.numberOfPortions = numberOfPortions;
    this.ingredients = ingredients;
  }

  public static Recipe from(NonnegativeInteger numberOfPortions,
      Map<Ingredient, NonnegativeFraction> ingredients) {
    return new Recipe(numberOfPortions, ingredients);
  }

  public static Recipe from(NonnegativeInteger numberOfPortions,
      List<QuantitativeIngredient> ingredients) {
    Map<Ingredient, NonnegativeFraction> ingredientMap = ingredients.stream()
        .collect(groupingBy(QuantitativeIngredient::getIngredient,
            reducing(ZERO,
                QuantitativeIngredient::getAmount,
                (amount1, amount2) -> amount1.add(amount2))));
    return new Recipe(numberOfPortions, ingredientMap);
  }

  public static Recipe createRecipe() {
    return new Recipe(ONE, new HashMap<>());
  }

  public NonnegativeInteger getNumberOfPortions() {
    return numberOfPortions;
  }

  public Map<Ingredient, NonnegativeFraction> getIngredientsFor(
      final NonnegativeInteger numberOfPeople) {
    return ingredients.entrySet().stream()
        .collect(toMap(Entry::getKey,
            entry -> entry.getValue()
                .multiplyBy(numberOfPeople)
                .divideBy(numberOfPortions)));
  }

  public Map<Ingredient, NonnegativeFraction> getIngredientsAsIs() {
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
    return ingredients.entrySet()
        .stream()
        .map(entry -> create(entry.getKey(), mapValues.apply(entry.getValue())))
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
