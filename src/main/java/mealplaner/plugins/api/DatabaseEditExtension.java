package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;

public interface DatabaseEditExtension {
  FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Meal> meals);
}
