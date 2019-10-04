// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV2ToXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3ToXml;
import static mealplaner.io.xml.util.FactsAdapter.extractFacts;
import static mealplaner.io.xml.util.FactsAdapter.extractUnknownFacts;

import java.util.ArrayList;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v2.MealXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.plugins.cookingtime.CookingTimeFact;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;

public final class MealAdapter {
  private MealAdapter() {
  }

  public static MealXml convertMealV2ToXml(Meal meal) {
    return new MealXml(
        meal.getId(),
        meal.getName(),
        meal.getTypedMealFact(CookingTimeFact.class).getCookingTime(),
        meal.getSidedish(),
        meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil(),
        meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference(),
        meal.getCourseType(),
        meal.getDaysPassed(),
        meal.getComment(),
        convertRecipeV2ToXml(meal.getRecipe()));
  }

  public static mealplaner.io.xml.model.v3.MealXml convertMealV3ToXml(Meal meal) {
    var mealFacts = new ArrayList<>();
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
        meal.getSidedish(),
        meal.getCourseType(),
        meal.getDaysPassed(),
        meal.getComment(),
        mealFacts,
        convertRecipeV3ToXml(meal.getRecipe()));
  }

  public static Meal convertMealV2FromXml(MealplanerData data, MealXml meal) {
    return MealBuilder.meal()
        .id(meal.uuid)
        .name(meal.name)
        .cookingTime(meal.cookingTime)
        .sidedish(meal.sidedish)
        .obligatoryUtensil(meal.obligatoryUtensil)
        .cookingPreference(meal.cookingPreference)
        .courseType(meal.courseType)
        .daysPassed(nonNegative(meal.daysPassed))
        .comment(meal.comment)
        .optionalRecipe(convertRecipeV2FromXml(data, meal.recipe))
        .create();
  }

  public static Meal convertMealV3FromXml(
      MealplanerData data, mealplaner.io.xml.model.v3.MealXml meal, PluginStore plugins) {
    return MealBuilder.mealWithValidator(plugins)
        .id(meal.uuid)
        .name(meal.name)
        .sidedish(meal.sidedish)
        .courseType(meal.courseType)
        .daysPassed(nonNegative(meal.daysPassed))
        .comment(meal.comment)
        .optionalRecipe(convertRecipeV3FromXml(data, meal.recipe))
        .addMealMap(extractFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()))
        .addHiddenMeals(extractUnknownFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()))
        .create();
  }
}
