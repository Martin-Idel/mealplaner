// SPDX-License-Identifier: MIT

package mealplaner.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.settings.DefaultSettings;

public interface DataStore {
  void register(DataStoreListener listener);

  void deregister(DataStoreListener listener);

  int getDaysPassed();

  Proposal getLastProposal();

  LocalDate getTime();

  List<Meal> getMeals();

  Optional<Meal> getMeal(UUID uuid);

  DefaultSettings getDefaultSettings();

  List<Ingredient> getIngredients();
}
