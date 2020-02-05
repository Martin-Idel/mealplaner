// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.databaseedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.editing.NonemptyTextCellEditor;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.api.MealEditExtension;

final class DatabaseEditTable {
  private DatabaseEditTable() {
  }

  public static Table createTable(
      ButtonPanelEnabling buttonPanel,
      List<Meal> meals,
      UnaryOperator<Optional<Recipe>> editRecipe,
      Collection<MealEditExtension> extensions) {
    var tableModelBuilder = createNewTable()
        .withRowCount(meals::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("mealNameColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, name) -> from(meal).name(name).create())
            .getValueFromOrderedList(meals, Meal::getName)
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .overwriteTableCellEditor(new NonemptyTextCellEditor())
            .buildWithOrderNumber(0))
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName(BUNDLES.message("cookedLastTimeColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, day) -> from(meal).daysPassed(day).create())
            .getValueFromOrderedList(meals, Meal::getDaysPassed)
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .buildWithOrderNumber(40))
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("recipeEditColum"))
            .getRowValueFromUnderlyingModel(
                row -> meals.get(row).getRecipe().isPresent()
                    ? BUNDLES.message("editRecipeButtonLabel")
                    : BUNDLES.message("createRecipeButtonLabel"))
            .buildWithOrderNumber(110))
        .addListenerToThisColumn(row -> {
          Optional<Recipe> recipe = meals.get(row).getRecipe();
          Optional<Recipe> editedRecipe = editRecipe.apply(recipe);
          Meal newMeal = from(meals.get(row)).optionalRecipe(editedRecipe).create();
          meals.set(row, newMeal);
          buttonPanel.enableButtons();
        });
    for (var extension : extensions) {
      tableModelBuilder = extension.addTableColumns(tableModelBuilder, meals, buttonPanel);
    }
    return tableModelBuilder.buildTable();
  }

}
