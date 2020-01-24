package mealplaner.gui.dialogs.recepies;

import javax.swing.table.DefaultTableCellRenderer;

import mealplaner.model.recipes.Ingredient;

// TODO: Make generic
public class FlexibleClassRenderer extends DefaultTableCellRenderer {
  protected void setValue(Object value) {
    if (value instanceof Ingredient) {
      this.setText(((Ingredient) value).getName());
    } else {
      this.setText("");
    }
  }
}
