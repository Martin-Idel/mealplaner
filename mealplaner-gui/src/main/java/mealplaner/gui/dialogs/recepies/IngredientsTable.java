// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeFractionContent;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;

import java.awt.Component;
import java.util.List;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.QuantitativeIngredient;

public final class IngredientsTable {
  private IngredientsTable() {
  }

  // TODO: We can get a null value if we don't select anything in the combobox. We should handle this gracefully
  public static Table setupTable(
      List<QuantitativeIngredient> ingredients, List<Ingredient> ingredientList) {
    return createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(Ingredient.class)
            .isEditable()
            .withColumnName(BUNDLES.message("ingredientNameColumn"))
            .setValueToOrderedImmutableList(ingredients,
                (quantitativeIngredient, ingredient) -> createQuantitativeIngredient(
                    ingredient, ingredient.getPrimaryMeasure(), quantitativeIngredient.getAmount())
            )
            .alsoUpdatesCellsOfColumns(2)
            .getValueFromOrderedList(ingredients, QuantitativeIngredient::getIngredient)
            .isEditable()
            .overwriteTableCellEditor(
                autoCompleteCellEditor(ingredientList))
            .overwriteTableCellRenderer(new FlexibleClassRenderer())
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
            .overwriteTableCellEditor(new FlexibleJComboBoxEditor(0))
            .isEditableIf(row -> (row < ingredients.size())
                && !ingredients.get(row).getIngredient().getMeasures().getSecondaries().isEmpty())
            .build())
        .addDefaultRowToUnderlyingModel(() -> ingredients.add(QuantitativeIngredient.DEFAULT))
        .deleteRowsOnDelete(row -> ingredients.remove((int) row))
        .buildDynamicTable();
  }

  // TODO: Make generic
  public static <T> ComboBoxCellEditor autoCompleteCellEditor(List<T> list) {
    JComboBox<T> autoCompleteBox = new JComboBox<>(new Vector<>(list));
    autoCompleteBox.setRenderer(new ObjectRenderer());
    AutoCompleteDecorator.decorate(autoCompleteBox, new ObjectToStringConverter() {
      @Override
      public String getPreferredStringForItem(Object item) {
        if (item instanceof Ingredient) {
          return ((Ingredient) item).getName();
        } else {
          return "";
        }
      }
    });
    return new ComboBoxCellEditor(autoCompleteBox);
  }

  // TODO: Make generic
  public static class ObjectRenderer extends BasicComboBoxRenderer {
    @Override
    public Component getListCellRendererComponent(
        JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

      if (value instanceof Ingredient) {
        setText(((Ingredient) value).getName());
      } else {
        setText("");
      }

      return this;
    }
  }
}
