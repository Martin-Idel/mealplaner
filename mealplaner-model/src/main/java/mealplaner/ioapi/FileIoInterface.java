// SPDX-License-Identifier: MIT

package mealplaner.ioapi;

import java.util.Optional;

import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;

public interface FileIoInterface {
  MealplanerData loadDatabase(PluginStore pluginStore);

  void saveDatabase(MealplanerData mealPlan);

  void savePart(MealplanerData mealPlan, DataParts part);

  Optional<MealplanerData> loadBackup(PluginStore pluginStore);

  void createBackup(MealplanerData mealPlan);
}
