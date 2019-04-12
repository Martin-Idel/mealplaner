// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.SwingUtilityMethods.autoCompleteCellEditor;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.QuantitativeIngredient.create;

import java.util.List;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.gui.tables.Table;
import mealplaner.commons.gui.tables.TableColumnBuilder;
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
                  return create(newIngredient, ingredient.getAmount());
                })
            .alsoUpdatesCellsOfColumns(2)
            .getValueFromOrderedList(ingredients,
                ingredient -> ingredient.getIngredient().getName())
            .isEditable()
            .setDefaultValueForEmptyRow("")
            .overwriteTableCellEditor(
                autoCompleteCellEditor(ingredientList, Ingredient::getName))
            .build())
        .addColumn(TableColumnBuilder.withNonnegativeFractionContent()
            .withColumnName(BUNDLES.message("ingredientAmountColumn"))
            .getValueFromOrderedList(ingredients,
                    QuantitativeIngredient::getAmount)
            .setValueToOrderedImmutableList(ingredients,
                (ingredient, amount) -> create(ingredient.getIngredient(), amount))
            .isEditable()
            .setDefaultValueForEmptyRow(NonnegativeFraction.ZERO)
            .build())
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("ingredientMeasureColumn"))
            .getValueFromOrderedList(ingredients,
                ingredient -> ingredient.getIngredient().getMeasure())
            .setDefaultValueForEmptyRow(Measure.NONE)
            .build())
        .addDefaultRowToUnderlyingModel(() -> ingredients.add(QuantitativeIngredient.DEFAULT))
        .deleteRowsOnDelete(row -> ingredients.remove((int) row))
        .buildDynamicTable();
  }
}
