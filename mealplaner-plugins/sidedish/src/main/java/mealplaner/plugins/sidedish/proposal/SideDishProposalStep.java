package mealplaner.plugins.sidedish.proposal;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.sidedish.mealextension.Sidedish;
import mealplaner.plugins.sidedish.mealextension.SidedishFact;

public class SideDishProposalStep implements ProposalBuilderStep {
  private SideDish sideDish = new SideDish();

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return meals.map(pair -> takeSideDishIntoAccount(pair, sideDish));
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToDeserts(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals;
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToEntries(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return meals.filter(entry -> useDifferentSidedish(entry.left, main));
  }

  @Override
  public void setupProposalStep(Map<UUID, Meal> mealData) {
    setCurrentSideDishFromHistory(mealData);
  }

  @Override
  public void incrementProposalStepBetweenMeals(Meal proposedMeal) {
    updateCurrentSideDish(proposedMeal);
  }

  private static boolean useDifferentSidedish(Meal meal, Meal main) {
    return meal.getTypedMealFact(SidedishFact.class).getSidedish() == Sidedish.NONE
        || meal.getTypedMealFact(SidedishFact.class).getSidedish()
        != main.getTypedMealFact(SidedishFact.class).getSidedish();
  }

  private Pair<Meal, Integer> takeSideDishIntoAccount(Pair<Meal, Integer> pair, SideDish sideDish) {
    return (pair.left.getTypedMealFact(SidedishFact.class).getSidedish() == sideDish.current)
        ? Pair.of(pair.left, (int) (pair.right * (3f - sideDish.inARow) / 2))
        : pair;
  }

  private void setCurrentSideDishFromHistory(Map<UUID, Meal> mealData) {
    sideDish.reset();
    Iterator<Meal> mealList = getLastCookedDishes(mealData).iterator();
    if (mealList.hasNext()) {
      sideDish.current = mealList.next().getTypedMealFact(SidedishFact.class).getSidedish();
      while (sideDish.inARow < 3 && mealList.hasNext()
          && mealList.next().getTypedMealFact(SidedishFact.class).getSidedish() == sideDish.current) {
        sideDish.incrementInARow();
      }
    }
  }

  private List<Meal> getLastCookedDishes(Map<UUID, Meal> mealData) {
    return mealData.values().stream()
        .sorted(comparing(Meal::getDaysPassed))
        .limit(3)
        .collect(toList());
  }

  private void updateCurrentSideDish(Meal nextProposal) {
    sideDish = (sideDish.current == nextProposal.getTypedMealFact(SidedishFact.class).getSidedish())
        ? sideDish.incrementInARow()
        : sideDish.resetToSideDish(nextProposal.getTypedMealFact(SidedishFact.class).getSidedish());
  }

}
