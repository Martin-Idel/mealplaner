// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.proposal;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toSet;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.seasonal.ingredientextension.Seasonality;
import mealplaner.plugins.seasonal.ingredientextension.SeasonalityFact;

public class SeasonProposalStep implements ProposalBuilderStep {
  public static final float IN_SEASON_VERY_SEASONAL_MULTIPLIER = 0.5f;
  public static final float IN_SEASON_MILDLY_SEASONAL_MULTIPLIER = 0.75f;
  public static final float OUT_SEASON_VERY_SEASONAL_MULTIPLIER = 3f;
  public static final float OUT_SEASON_MILDLY_SEASONAL_MULTIPLIER = 1.5f;

  private LocalDate proposalDate;

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    return meals.map(this::applySeasonalityMultiplier);
  }

  @Override
  public void setupProposalStep(Map<UUID, Meal> mealData) {
    proposalDate = now();
  }

  private Pair<Meal, Integer> applySeasonalityMultiplier(Pair<Meal, Integer> pair) {
    Meal meal = pair.left;
    int originalValue = pair.right;

    List<QuantitativeIngredient> ingredients = meal.getRecipe()
        .map(mealplaner.model.recipes.Recipe::getIngredientsAsIs)
        .orElse(List.of());

    Set<Ingredient> uniqueIngredients = ingredients.stream()
        .map(QuantitativeIngredient::getIngredient)
        .collect(toSet());

    double multiplier = calculateSeasonalityMultiplier(uniqueIngredients);

    return Pair.of(meal, (int) (originalValue * multiplier));
  }

  private double calculateSeasonalityMultiplier(Set<Ingredient> ingredients) {
    double totalMultiplier = 1.0;
    String currentMonth = proposalDate.getMonth().toString();

    for (Ingredient ingredient : ingredients) {
      SeasonalityFact seasonalityFact = ingredient.getTypedIngredientFact(SeasonalityFact.class);
      if (seasonalityFact != null) {
        totalMultiplier *= calculateIngredientMultiplier(seasonalityFact, currentMonth);
      }
    }

    return totalMultiplier;
  }

  private double calculateIngredientMultiplier(SeasonalityFact seasonalityFact, String currentMonth) {
    Seasonality seasonality = seasonalityFact.getSeasonality();
    if (seasonality == Seasonality.NON_SEASONAL) {
      return 1.0;
    }

    Set<String> mainSeason = seasonalityFact.getMainSeasonMonths();
    Set<String> offSeason = seasonalityFact.getOffSeasonMonths();

    if (mainSeason.contains(currentMonth)) {
      return (seasonality == Seasonality.VERY_SEASONAL)
          ? IN_SEASON_VERY_SEASONAL_MULTIPLIER
          : IN_SEASON_MILDLY_SEASONAL_MULTIPLIER;
    } else if (offSeason.contains(currentMonth)) {
      return 1.0;
    } else {
      return (seasonality == Seasonality.VERY_SEASONAL)
          ? OUT_SEASON_VERY_SEASONAL_MULTIPLIER
          : OUT_SEASON_MILDLY_SEASONAL_MULTIPLIER;
    }
  }
}
