package mealplaner.plugins.plugins.utensil.proposal;

import java.util.stream.Stream;

import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.api.ProposalBuilderStep;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.plugins.utensil.settingextension.CasseroleSubSetting;

public class UtensilProposalStep implements ProposalBuilderStep {
  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestions(Stream<Pair<Meal, Integer>> meals, Settings settings) {
    var cookingUtensilSettings = createCookingUtensilSettings(settings);
    return meals.filter(meal -> !cookingUtensilSettings.prohibits(meal.left));
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToDeserts(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return applyPluginSuggestions(meals, settings)
        .filter(meal -> useDifferentUtensil(meal.left, main));
  }

  @Override
  public Stream<Pair<Meal, Integer>> applyPluginSuggestionsToEntries(
      Stream<Pair<Meal, Integer>> meals, Settings settings, Meal main) {
    return applyPluginSuggestions(meals, settings)
        .filter(meal -> useDifferentUtensil(meal.left, main));
  }

  private static CookingUtensilSetting createCookingUtensilSettings(Settings settings) {
    var casseroleSettings = CookingUtensilSetting.createCookingUtensilSettings();
    casseroleSettings.setNumberOfPeople(settings.getNumberOfPeople());
    casseroleSettings.setCasseroleSettings(
        settings.getTypedSubSetting(CasseroleSubSetting.class).getCasseroleSettings());
    return casseroleSettings;
  }

  private static boolean useDifferentUtensil(Meal meal, Meal main) {
    switch (main.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil()) {
      case CASSEROLE:
        return meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil() != ObligatoryUtensil.CASSEROLE;
      case PAN:
        return meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil() != ObligatoryUtensil.PAN;
      default:
        return true;
    }
  }
}
