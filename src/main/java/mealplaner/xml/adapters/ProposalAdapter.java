package mealplaner.xml.adapters;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.ProposedMenuAdapter.convertProposedMenuFromXml;
import static mealplaner.xml.adapters.ProposedMenuAdapter.convertProposedMenuToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsFromXml;

import java.util.List;
import java.util.Optional;

import mealplaner.MealplanerData;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.xml.model.v1.MealXml;
import mealplaner.xml.model.v2.ProposalXml;
import mealplaner.xml.model.v2.ProposedMenuXml;

public final class ProposalAdapter {
  private ProposalAdapter() {
  }

  public static ProposalXml convertProposalToXml(Proposal proposal) {
    List<ProposedMenuXml> mealList = proposal.getProposalList()
        .stream()
        .map(meal -> convertProposedMenuToXml(meal))
        .collect(toList());

    return new ProposalXml(mealList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static Proposal convertProposalFromXml(MealplanerData data, ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(meal -> convertProposedMenuFromXml(meal))
            .collect(toList()),
        proposalData.date);
  }

  public static Proposal convertProposalFromXml(MealplanerData data,
      mealplaner.xml.model.v1.ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(meal -> convertToMealFromMealplaner(data, meal))
            .collect(toList()),
        proposalData.settingsList
            .stream()
            .map(setting -> convertSettingsFromXml(setting))
            .collect(toList()),
        proposalData.date);
  }

  private static Meal convertToMealFromMealplaner(MealplanerData data, MealXml mealXml) {
    List<Meal> meals = data.getMeals();
    Optional<Meal> findFirst = meals.stream().filter(meal -> compareMeals(data, meal, mealXml))
        .findFirst();
    if (findFirst.isPresent()) {
      return findFirst.get();
    }
    throw new MealException("Save file corrupted. Meal could not be found in list.");
  }

  private static boolean compareMeals(MealplanerData data, Meal meal, MealXml mealXml) {
    return meal.getName().equals(mealXml.name)
        && meal.getCookingTime().equals(mealXml.cookingTime)
        && meal.getSidedish().equals(mealXml.sidedish)
        && meal.getObligatoryUtensil().equals(mealXml.obligatoryUtensil)
        && meal.getCookingPreference().equals(mealXml.cookingPreference)
        && meal.getComment().equals(mealXml.comment)
        && meal.getDaysPassed().value == mealXml.daysPassed
        && meal.getRecipe().equals(RecipeAdapter.convertRecipeFromXml(data, mealXml.recipe));
  }
}
