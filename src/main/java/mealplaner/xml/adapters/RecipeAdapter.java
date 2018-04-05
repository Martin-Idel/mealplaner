package mealplaner.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Recipe.from;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientFromXml;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import mealplaner.MealplanerData;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.v2.RecipeXml;

// TODO Better error messages.
public final class RecipeAdapter {
  private RecipeAdapter() {
  }

  public static RecipeXml convertRecipeToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      Map<UUID, Integer> integerMap = recipe.getIngredientsAsIs()
          .entrySet()
          .stream()
          .collect(toMap(e -> e.getKey().getId(), e -> e.getValue().value));
      return new RecipeXml(recipe.getNumberOfPortions().value, integerMap);
    }
    return null;
  }

  public static Optional<Recipe> convertRecipeFromXml(MealplanerData data, RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<UUID, Ingredient> ingredients = data.getIngredients().stream()
        .collect(toMap(Ingredient::getId, identity()));
    for (UUID id : recipe.uuid.keySet()) {
      if (ingredients.get(id) == null) {
        throw new MealException("Save file corrupted. Ingredient was not present in list.");
      }
    }
    Map<Ingredient, NonnegativeInteger> nonnegativeIntegerMap = recipe.uuid
        .entrySet()
        .stream()
        .collect(toMap(entry -> ingredients.get(entry.getKey()),
            entry -> nonNegative(entry.getValue())));
    return of(from(nonNegative(recipe.numberOfPortions),
        nonnegativeIntegerMap));
  }

  public static Optional<Recipe> convertRecipeFromXml(MealplanerData data,
      mealplaner.xml.model.v1.RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<Ingredient, NonnegativeInteger> nonnegativeIntegerMap = recipe.ingredients
        .entrySet()
        .stream()
        .collect(toMap(e -> convertIngredientFromXml(e.getKey()),
            e -> nonNegative(e.getValue())));
    return of(from(nonNegative(recipe.numberOfPortions), nonnegativeIntegerMap));
  }
}
