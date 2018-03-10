package mealplaner.xml;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.xml.JaxHelper.read;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.xml.model.IngredientXml;
import mealplaner.xml.model.MealdatabaseXml;
import mealplaner.xml.model.RecipeXml;

public final class MealsReader {
  private MealsReader() {
  }

  public static List<Meal> loadXml(String filePath) {
    int versionNumber = VersionControl.getVersion(filePath);
    if (versionNumber == 1) {
      MealdatabaseXml database = read(filePath, MealdatabaseXml.class);
      return convertDataBaseFromXml(database);
    } else {
      // TODO: Delete once saves have been ported
      return new ArrayList<>();
      // try {
      // return MealplanerFileLoader.load(filePath).getMeals();
      // } catch (MealException | IOException e) {
      // return new ArrayList<>();
      // }
    }
  }

  private static List<Meal> convertDataBaseFromXml(MealdatabaseXml data) {
    List<Meal> modelMeals = new ArrayList<>();
    data.meals.stream()
        .map(meal -> Meal.createMeal(
            meal.name,
            meal.cookingTime,
            meal.sidedish,
            meal.obligatoryUtensil,
            meal.cookingPreference,
            nonNegative(meal.daysPassed),
            meal.comment,
            convertRecipeFromXml(meal.recipe)))
        .forEach(meal -> modelMeals.add(meal));
    return modelMeals;
  }

  private static Optional<Recipe> convertRecipeFromXml(RecipeXml recipe) {
    if (recipe == null) {
      return empty();
    }
    Map<Ingredient, NonnegativeInteger> nonnegativeIntegerMap = recipe.ingredients
        .entrySet()
        .stream()
        .collect(toMap(e -> convertIngredientFromXml(e.getKey()), e -> nonNegative(e.getValue())));
    return of(Recipe.from(nonNegative(recipe.numberOfPortions), nonnegativeIntegerMap));
  }

  public static Ingredient convertIngredientFromXml(IngredientXml ingredient) {
    return Ingredient.ingredient(ingredient.name, ingredient.type, ingredient.measure);
  }
}
