// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.model.recipes.Recipe.from;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.model.v3.QuantitativeIngredientXml;
import mealplaner.io.xml.model.v3.RecipeXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

final class RecipeAdapter {
  private RecipeAdapter() {
  }

  public static RecipeXml convertRecipeV3ToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      List<QuantitativeIngredientXml> ingredients = recipe.getIngredientsAsIs()
          .stream()
          .map(quantitativeIngredient -> new QuantitativeIngredientXml(
              quantitativeIngredient.getIngredient().getId(),
              quantitativeIngredient.getAmount(),
              quantitativeIngredient.getMeasure()))
          .collect(toList());
      return new RecipeXml(recipe.getNumberOfPortions().value, ingredients);
    }
    return null;
  }

  public static Optional<Recipe> convertRecipeV3FromXml(MealplanerData data, RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<UUID, Ingredient> ingredients = data.getIngredients().stream()
        .collect(toMap(Ingredient::getId, identity()));
    for (QuantitativeIngredientXml quantitativeIngredientXml : recipe.quantitativeIngredients) {
      if (ingredients.get(quantitativeIngredientXml.uuid) == null) {
        throw new MealException("Save file corrupted. Ingredient was not present in list.");
      }
    }
    List<QuantitativeIngredient> quantitativeIngredients = recipe.quantitativeIngredients
        .stream()
        .map(quantIn -> createQuantitativeIngredient(ingredients.get(quantIn.uuid), quantIn.measure, quantIn.amount))
        .collect(toList());
    return of(from(nonNegative(recipe.numberOfPortions),
        quantitativeIngredients));
  }
}
