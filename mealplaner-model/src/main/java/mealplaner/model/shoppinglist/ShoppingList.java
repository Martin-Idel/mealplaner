// SPDX-License-Identifier: MIT

package mealplaner.model.shoppinglist;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

public final class ShoppingList {
  private final Map<Ingredient, NonnegativeFraction> shoppingListMap;

  private ShoppingList(Map<Ingredient, NonnegativeFraction> recipes) {
    shoppingListMap = recipes;
  }

  public static ShoppingList from(List<Pair<Recipe, NonnegativeInteger>> recipes) {
    Map<Ingredient, NonnegativeFraction> shoppingList = recipes.stream()
        .map(pair -> pair.left.getIngredientsWithPrimaryMeasureFor(pair.right))
        .flatMap(Collection::stream)
        .collect(toMap(QuantitativeIngredient::getIngredient,
            QuantitativeIngredient::getAmount,
            NonnegativeFraction::add));
    return new ShoppingList(shoppingList);
  }

  public List<QuantitativeIngredient> getList() {
    return shoppingListMap.entrySet().stream()
        .map(entry -> createQuantitativeIngredient(
            entry.getKey(), entry.getKey().getMeasures().getPrimaryMeasure(), entry.getValue()))
        .sorted(Comparator.comparing(ingredient -> ingredient.getIngredient().getType()))
        .collect(toList());
  }

  public Map<Ingredient, NonnegativeFraction> getMap() {
    return shoppingListMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShoppingList that = (ShoppingList) o;
    return Objects.equals(shoppingListMap, that.shoppingListMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shoppingListMap);
  }
}
