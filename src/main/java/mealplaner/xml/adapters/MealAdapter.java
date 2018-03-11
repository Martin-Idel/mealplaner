package mealplaner.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.xml.adapters.RecipeAdapter.convertRecipeFromXml;
import static mealplaner.xml.adapters.RecipeAdapter.convertRecipeToXml;

import mealplaner.model.Meal;
import mealplaner.xml.model.MealXml;

public final class MealAdapter {
  private MealAdapter() {
  }

  public static MealXml convertMealToXml(Meal meal) {
    return new MealXml(
        meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getDaysPassed(),
        meal.getComment(),
        convertRecipeToXml(meal.getRecipe()));
  }

  public static Meal convertMealFromXml(MealXml meal) {
    return createMeal(
        meal.name,
        meal.cookingTime,
        meal.sidedish,
        meal.obligatoryUtensil,
        meal.cookingPreference,
        nonNegative(meal.daysPassed),
        meal.comment,
        convertRecipeFromXml(meal.recipe));
  }
}
