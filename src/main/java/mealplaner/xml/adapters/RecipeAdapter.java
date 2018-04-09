package mealplaner.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Recipe.from;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import mealplaner.MealplanerData;
import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.v1.IngredientXml;
import mealplaner.xml.model.v2.RecipeXml;

// TODO Better error messages.
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

  public static Optional<Recipe> convertRecipeFromXml(MealplanerData data,
      mealplaner.xml.model.v1.RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    List<Ingredient> ingredients = data.getIngredients();
    validateIngredients(recipe.ingredients, ingredients);

    Map<Ingredient, NonnegativeFraction> nonnegativeIntegerMap = recipe.ingredients
        .entrySet()
        .stream()
        .collect(toMap(e -> findCorrectIngredient(e.getKey(), ingredients),
            e -> NonnegativeFraction.fraction(e.getValue(), 1)));
    return of(from(nonNegative(recipe.numberOfPortions), nonnegativeIntegerMap));
  }

  private static void validateIngredients(Map<IngredientXml, Integer> recipe,
      List<Ingredient> ingredients) {
    if (!recipe.keySet()
        .stream()
        .allMatch(ingredient -> ingredients.stream()
            .filter(ing -> ing.getName().equals(ingredient.name)
                && ing.getType().equals(ingredient.type)
                && ing.getMeasure().equals(ingredient.measure))
            .findFirst().isPresent())) {
      throw new MealException("Not all ingredients are present in Mealplaner");
    }
  }

  private static Ingredient findCorrectIngredient(IngredientXml ingredient,
      List<Ingredient> ingredients) {
    return ingredients.stream()
        .filter(ing -> ing.getName().equals(ingredient.name)
            && ing.getType().equals(ingredient.type)
            && ing.getMeasure().equals(ingredient.measure))
        .findFirst()
        .get();
  }
}
