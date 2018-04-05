package mealplaner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.recipes.model.Ingredient;

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
