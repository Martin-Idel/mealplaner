package mealplaner.model.shoppinglist;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.model.recipes.QuantitativeIngredient.create;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

public final class ShoppingList {
  private final Map<Ingredient, NonnegativeFraction> shoppingList;

  private ShoppingList(Map<Ingredient, NonnegativeFraction> recipes) {
    shoppingList = recipes;
  }

  public static ShoppingList from(List<Pair<Recipe, NonnegativeInteger>> recipes) {
    Map<Ingredient, NonnegativeFraction> shoppingList = recipes.stream()
        .map(pair -> pair.left.getIngredientsFor(pair.right))
        .map(Map::entrySet)
        .flatMap(Collection::stream)
        .collect(toMap(Map.Entry::getKey,
            Map.Entry::getValue,
            (a, b) -> a.add(b)));
    return new ShoppingList(shoppingList);
  }

  public static ShoppingList emptyList() {
    return new ShoppingList(new HashMap<>());
  }

  public List<QuantitativeIngredient> getList() {
    return shoppingList.entrySet().stream()
        .map(entry -> create(entry.getKey(), entry.getValue()))
        .sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getType()
            .compareTo(ingredient2.getIngredient().getType()))
        .collect(toList());
  }

  public Map<Ingredient, NonnegativeFraction> getMap() {
    return shoppingList;
  }
}