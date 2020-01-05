package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.recipes.Ingredient;

public interface IngredientEditExtension {
  FlexibleTableBuilder addTableColumns(FlexibleTableBuilder table, List<Ingredient> ingredients);
}
