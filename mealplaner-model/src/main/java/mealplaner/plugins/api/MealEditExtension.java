// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;

public interface MealEditExtension {
  FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table,
      List<Meal> meals,
      ButtonPanelEnabling buttonPanelEnabling);
}
