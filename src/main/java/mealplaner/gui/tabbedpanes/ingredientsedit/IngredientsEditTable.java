// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

public final class IngredientsEditTable {
  private IngredientsEditTable() {
  }

  public static Table createTable(List<Ingredient> ingredients, ButtonPanelEnabling buttonPanel) {
    return createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("insertIngredientName"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getName())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newName) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    newName,
                    oldIngredient.getType(),
                    oldIngredient.getMeasure()))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(IngredientType.class)
            .withColumnName(BUNDLES.message("insertTypeLength"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getType())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newType) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    oldIngredient.getName(),
                    newType,
                    oldIngredient.getMeasure()))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("insertMeasure"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getMeasure())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newMeasure) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    oldIngredient.getName(),
                    oldIngredient.getType(),
                    newMeasure))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .buildTable();
  }

}
