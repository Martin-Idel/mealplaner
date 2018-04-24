package mealplaner.io.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.Recipe.from;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.io.xml.model.v2.RecipeXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Recipe;

public final class RecipeAdapter {
  private RecipeAdapter() {
  }

  public static RecipeXml convertRecipeToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      Map<UUID, NonnegativeFraction> integerMap = recipe.getIngredientsAsIs()
          .entrySet()
          .stream()
          .collect(toMap(e -> e.getKey().getId(), e -> e.getValue()));
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
    Map<Ingredient, NonnegativeFraction> nonnegativeIntegerMap = recipe.uuid
        .entrySet()
        .stream()
        .collect(toMap(entry -> ingredients.get(entry.getKey()), Entry::getValue));
    return of(from(nonNegative(recipe.numberOfPortions),
        nonnegativeIntegerMap));
  }
}
