package mealplaner;

import java.time.LocalDate;
import java.util.List;

import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.recipes.model.Ingredient;

public interface DataStore {
  void register(DataStoreListener listener);

  int getDaysPassed();

  Proposal getLastProposal();

  LocalDate getTime();

  List<Meal> getMeals();

  DefaultSettings getDefaultSettings();

  List<Ingredient> getIngredients();
}
