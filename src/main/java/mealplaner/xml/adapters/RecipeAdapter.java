package mealplaner.xml.adapters;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Recipe.from;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientFromXml;
import static mealplaner.xml.adapters.IngredientAdapter.convertIngredientToXml;

import java.util.Map;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.RecipeXml;

public final class RecipeAdapter {
  private RecipeAdapter() {
  }

  public static RecipeXml convertRecipeToXml(Optional<Recipe> optionalRecipe) {
    if (optionalRecipe.isPresent()) {
      Recipe recipe = optionalRecipe.get();
      Map<IngredientXml, Integer> integerMap = recipe.getIngredientsAsIs()
          .entrySet()
          .stream()
          .collect(toMap(e -> convertIngredientToXml(e.getKey()), e -> e.getValue().value));
      return new RecipeXml(recipe.getNumberOfPortions().value,
          integerMap);
    }
    return null;
  }

  public static Optional<Recipe> convertRecipeFromXml(RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<Ingredient, NonnegativeInteger> nonnegativeIntegerMap = recipe.ingredients
        .entrySet()
        .stream()
        .collect(toMap(e -> convertIngredientFromXml(e.getKey()), e -> nonNegative(e.getValue())));
    return of(from(nonNegative(recipe.numberOfPortions), nonnegativeIntegerMap));
  }
}
