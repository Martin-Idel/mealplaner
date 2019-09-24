// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.IngredientBuilder.from;

import java.util.Collection;
import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.plugins.api.IngredientEditExtension;

final class IngredientsEditTable {
  private IngredientsEditTable() {
  }

  public static Table createTable(
      List<Ingredient> ingredients,
      ButtonPanelEnabling buttonPanel,
      Collection<IngredientEditExtension> ingredientEditExtensions) {
    var tableModelBuilder = createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("insertIngredientName"))
            .getValueFromOrderedList(ingredients, Ingredient::getName)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newName) -> from(oldIngredient).withName(newName).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .build())
        .addColumn(withEnumContent(IngredientType.class)
            .withColumnName(BUNDLES.message("insertTypeLength"))
            .getValueFromOrderedList(ingredients, Ingredient::getType)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newType) -> from(oldIngredient).withType(newType).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .build())
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("insertMeasure"))
            .getValueFromOrderedList(ingredients, Ingredient::getPrimaryMeasure)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newMeasure) -> from(oldIngredient).withPrimaryMeasure(newMeasure).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .build());
    for (var ingredientEditExtension : ingredientEditExtensions) {
      ingredientEditExtension.addTableColumns(tableModelBuilder, ingredients);
    }
    return tableModelBuilder.buildTable();
  }

}
