// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2ToXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3ToXml;
import static mealplaner.model.meal.Meal.createMeal;

import mealplaner.io.xml.model.v2.MealXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public final class MealAdapter {
  private MealAdapter() {
  }

  public static MealXml convertMealV2ToXml(Meal meal) {
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
        convertRecipeV2ToXml(meal.getRecipe()));
  }

  public static mealplaner.io.xml.model.v3.MealXml convertMealV3ToXml(Meal meal) {
    return new mealplaner.io.xml.model.v3.MealXml(
        meal.getId(),
        meal.getName(),
        meal.getCookingTime(),
        meal.getSidedish(),
        meal.getObligatoryUtensil(),
        meal.getCookingPreference(),
        meal.getCourseType(),
        meal.getDaysPassed(),
        meal.getComment(),
        convertRecipeV3ToXml(meal.getRecipe()));
  }

  public static Meal convertMealV2FromXml(MealplanerData data, MealXml meal) {
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
        convertRecipeV2FromXml(data, meal.recipe));
  }

  public static Meal convertMealV3FromXml(MealplanerData data, mealplaner.io.xml.model.v3.MealXml meal) {
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
        convertRecipeV3FromXml(data, meal.recipe));
  }
}
