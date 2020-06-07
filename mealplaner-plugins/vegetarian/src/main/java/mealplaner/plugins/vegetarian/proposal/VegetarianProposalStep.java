// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian.proposal;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.vegetarian.mealextension.VegetarianFact;

public class VegetarianProposalStep implements ProposalBuilderStep {
  int daysVegetarian = 0;

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return meals.map(pair -> {
      if (pair.left.getTypedMealFact(VegetarianFact.class).isVegetarian()) {
        return pair;
      } else {
        return Pair.of(pair.left, pair.right * daysVegetarian);
      }
    });
  }

  @Override
  public void setupProposalStep(Map<UUID, Meal> mealData) {
    getLastCookedDishesAsVegetarian(mealData);
  }

  @Override
  public void incrementProposalStepBetweenMeals(Meal proposedMeal) {
    daysVegetarian = proposedMeal.getTypedMealFact(VegetarianFact.class).isVegetarian()
        ? daysVegetarian + 1
        : 0;
  }

  private void getLastCookedDishesAsVegetarian(Map<UUID, Meal> mealData) {
    daysVegetarian = 0;
    var mealList = getLastCookedDishes(mealData).iterator();
    while (mealList.hasNext() && mealList.next().getTypedMealFact(VegetarianFact.class).isVegetarian()) {
      daysVegetarian++;
    }
  }

  private List<Meal> getLastCookedDishes(Map<UUID, Meal> mealData) {
    return mealData.values().stream()
        .sorted(comparing(Meal::getDaysPassed))
        .limit(3)
        .collect(toList());
  }
}
