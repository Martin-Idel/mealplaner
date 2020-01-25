// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeFractionContent;
import static mealplaner.commons.gui.tables.cells.AutoCompleteEditors.autoCompleteCellEditor;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultComboBoxModel;

import mealplaner.commons.gui.tables.Table;
import mealplaner.commons.gui.tables.cells.FlexibleClassRenderer;
import mealplaner.commons.gui.tables.cells.FlexibleJComboBoxEditor;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;

public final class IngredientsTable {
  private IngredientsTable() {
  }

  // TODO: Exclude primary measure from secondary measure dialog
  // TODO: Fix class cast exception and not being able to assign the empty value...
  // TODO: We can get a null value if we don't select anything in the combobox. We should handle this gracefully
  public static Table setupTable(
      List<QuantitativeIngredient> ingredients, List<Ingredient> ingredientList) {
    return createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(Ingredient.class)
            .isEditable()
            .withColumnName(BUNDLES.message("ingredientNameColumn"))
            .setValueToOrderedImmutableList(ingredients,
                (quantitativeIngredient, ingredient) ->
                    createQuantitativeIngredient(
                        ingredient, ingredient.getPrimaryMeasure(), quantitativeIngredient.getAmount())
            )
            .alsoUpdatesCellsOfColumns(2)
            .getValueFromOrderedList(ingredients, QuantitativeIngredient::getIngredient)
            .isEditable()
            .overwriteTableCellEditor(autoCompleteCellEditor(
                ingredientList, Ingredient.emptyIngredient(), IngredientsTable::ingredientToStringRepresentation))
            .overwriteTableCellRenderer(new FlexibleClassRenderer(IngredientsTable::ingredientToStringRepresentation))
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
        .addColumn(withContent(Measure.class)
            .withColumnName(BUNDLES.message("ingredientMeasureColumn"))
            .getValueFromOrderedList(ingredients, QuantitativeIngredient::getMeasure)
            .setValueToOrderedImmutableList(ingredients, (ingredient, measure) -> createQuantitativeIngredient(
                ingredient.getIngredient(), measure, ingredient.getAmount()
            ))
            .overwriteTableCellEditor(new FlexibleJComboBoxEditor<>(
                0, IngredientsTable::updateComboBoxAccordingToSecondaryMeasuresOfIngredients))
            .isEditableIf(row -> (row < ingredients.size())
                && !ingredients.get(row).getIngredient().getMeasures().getSecondaries().isEmpty())
            .build())
        .addDefaultRowToUnderlyingModel(() -> ingredients.add(QuantitativeIngredient.DEFAULT))
        .deleteRowsOnDelete(row -> ingredients.remove((int) row))
        .buildDynamicTable();
  }

  private static void updateComboBoxAccordingToSecondaryMeasuresOfIngredients(
      DefaultComboBoxModel<Measure> comboBoxModel, Object ingredient) {
    if (ingredient instanceof Ingredient) {
      var secondaries = ((Ingredient) ingredient).getMeasures().getSecondaries();
      comboBoxModel.addAll(secondaries.keySet());
      comboBoxModel.addElement(((Ingredient) ingredient).getPrimaryMeasure());
    } else {
      comboBoxModel.addAll(Arrays.asList(Measure.values()));
    }
  }

  private static String ingredientToStringRepresentation(Object object) {
    return (object instanceof Ingredient) ? ((Ingredient) object).getName() : "";
  }
}
