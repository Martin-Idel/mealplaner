// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2ToXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3ToXml;
import static mealplaner.io.xml.util.FactsAdapter.extractFacts;
import static mealplaner.io.xml.util.FactsAdapter.extractUnknownFacts;
import static mealplaner.model.meal.Meal.createMeal;

import java.util.ArrayList;
import java.util.stream.Collectors;

import mealplaner.plugins.PluginStore;
import mealplaner.io.xml.model.v2.MealXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealFact;

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
    var mealFacts = new ArrayList<Object>();
    mealFacts.addAll(
        meal.getMealFacts()
            .values()
            .stream()
            .map(MealFact::convertToXml)
            .collect(Collectors.toUnmodifiableList()));
    mealFacts.addAll(meal.getHiddenFacts());
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
        mealFacts,
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

  public static Meal convertMealV3FromXml(
      MealplanerData data, mealplaner.io.xml.model.v3.MealXml meal, PluginStore plugins) {
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
        extractFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()),
        extractUnknownFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()),
        convertRecipeV3FromXml(data, meal.recipe));
  }
}
