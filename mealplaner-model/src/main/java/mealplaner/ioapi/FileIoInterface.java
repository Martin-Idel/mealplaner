// SPDX-License-Identifier: MIT

package mealplaner.ioapi;

import java.util.List;
import java.util.Optional;

import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.plugins.PluginStore;

public interface FileIoInterface {
  MealplanerData loadDatabase(PluginStore pluginStore);

  void saveDatabase(MealplanerData mealPlan);

  void savePart(MealplanerData mealPlan, DataParts part);

  Optional<MealplanerData> loadBackup(PluginStore pluginStore);

  void createBackup(MealplanerData mealPlan);

  void saveShoppingList(List<QuantitativeIngredient> shoppingList);
}
