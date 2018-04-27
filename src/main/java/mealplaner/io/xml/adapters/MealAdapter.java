// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeFromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeToXml;
import static mealplaner.model.meal.Meal.createMeal;

import mealplaner.io.xml.model.v2.MealXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public final class MealAdapter {
  private MealAdapter() {
  }

  public static MealXml convertMealToXml(Meal meal) {
    return new MealXml(
        meal.getId(),
        meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getCourseType(),
        meal.getDaysPassed(),
        meal.getComment(),
        convertRecipeToXml(meal.getRecipe()));
  }

  public static Meal convertMealFromXml(MealplanerData data, MealXml meal) {
    return createMeal(
        meal.uuid,
        meal.name,
        meal.cookingTime,
        meal.sidedish,
        meal.obligatoryUtensil,
        meal.cookingPreference,
        meal.courseType,
        nonNegative(meal.daysPassed),
        meal.comment,
        convertRecipeFromXml(data, meal.recipe));
  }
}
