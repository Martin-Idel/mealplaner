// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeFractionContent;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.util.List;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;

public final class IngredientsTable {
  private IngredientsTable() {
  }

  public static Table setupTable(List<QuantitativeIngredient> ingredients,
      List<Ingredient> ingredientList) {
    return createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(String.class)
            .isEditable()
            .withColumnName(BUNDLES.message("ingredientNameColumn"))
            .setValueToOrderedImmutableList(ingredients,
                (ingredient, name) -> {
                  Ingredient newIngredient = ingredientList
                      .stream()
                      .filter(ing -> ing.getName().equals(name))
                      .findAny()
                      .orElse(Ingredient.emptyIngredient());
                  return createQuantitativeIngredient(
                      newIngredient, newIngredient.getPrimaryMeasure(), ingredient.getAmount());
                })
            .alsoUpdatesCellsOfColumns(2)
            .getValueFromOrderedList(ingredients,
                ingredient -> ingredient.getIngredient().getName())
            .isEditable()
            .overwriteTableCellEditor(
                autoCompleteCellEditor(ingredientList, Ingredient::getName))
            .build())
        .addColumn(withNonnegativeFractionContent()
            .withColumnName(BUNDLES.message("ingredientAmountColumn"))
            .getValueFromOrderedList(ingredients,
                    QuantitativeIngredient::getAmount)
            .setValueToOrderedImmutableList(ingredients,
                (ingredient, amount) -> createQuantitativeIngredient(
                    ingredient.getIngredient(), ingredient.getMeasure(), amount))
            .isEditable()
            .build())
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("ingredientMeasureColumn"))
            .getValueFromOrderedList(ingredients,
                ingredient -> ingredient.getIngredient().getPrimaryMeasure())
            .build())
        .addDefaultRowToUnderlyingModel(() -> ingredients.add(QuantitativeIngredient.DEFAULT))
        .deleteRowsOnDelete(row -> ingredients.remove((int) row))
        .buildDynamicTable();
  }
}
