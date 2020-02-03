// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.cells;

import java.awt.Color;
import java.awt.Component;
import java.util.function.Function;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FlexibleClassRenderer extends DefaultTableCellRenderer {
  private transient Function<Object, String> getStringRepresentation;
  private final Object defaultObject;

  public FlexibleClassRenderer(Function<Object, String> getStringRepresentation, Object defaultObject) {
    this.getStringRepresentation = getStringRepresentation;
    this.defaultObject = defaultObject;
  }

  protected void setValue(Object value) {
    this.setText(getStringRepresentation.apply(value));
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (defaultObject.equals(value)) {
      component.setForeground(Color.GRAY);
    }
    return component;
  }
}
