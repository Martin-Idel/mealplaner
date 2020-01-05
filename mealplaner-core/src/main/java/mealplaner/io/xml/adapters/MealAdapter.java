// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3FromXml;
import static mealplaner.io.xml.adapters.RecipeAdapter.convertRecipeV3ToXml;
import static mealplaner.io.xml.util.FactsAdapter.extractFacts;
import static mealplaner.io.xml.util.FactsAdapter.extractUnknownFacts;

import java.util.ArrayList;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v3.MealXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;

public final class MealAdapter {
  private MealAdapter() {
  }

  public static MealXml convertMealV3ToXml(Meal meal) {
    var mealFacts = new ArrayList<>();
    mealFacts.addAll(
        meal.getMealFacts()
            .values()
            .stream()
            .map(MealFact::convertToXml)
            .collect(Collectors.toUnmodifiableList()));
    mealFacts.addAll(meal.getHiddenFacts());
    return new MealXml(
        meal.getId(),
        meal.getName(),
        meal.getDaysPassed(),
        mealFacts,
        convertRecipeV3ToXml(meal.getRecipe()));
  }

  public static Meal convertMealV3FromXml(MealplanerData data, MealXml meal, PluginStore plugins) {
    return MealBuilder.mealWithValidator(plugins)
        .id(meal.uuid)
        .name(meal.name)
        .daysPassed(nonNegative(meal.daysPassed))
        .optionalRecipe(convertRecipeV3FromXml(data, meal.recipe))
        .addMealMap(extractFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()))
        .addHiddenMeals(extractUnknownFacts(meal.mealFacts, plugins.getRegisteredMealExtensions()))
        .create();
  }
}
