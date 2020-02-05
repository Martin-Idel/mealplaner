// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.cells;

import java.awt.Color;
import java.awt.Component;
import java.util.Optional;
import java.util.function.Function;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class FlexibleClassRenderer extends DefaultTableCellRenderer {
  private static final long serialVersionUID = 1L;
  private transient Function<Object, String> getStringRepresentation;
  private final transient Optional<Object> defaultObject;

  public FlexibleClassRenderer(Function<Object, String> getStringRepresentation, Object defaultObject) {
    this.getStringRepresentation = getStringRepresentation;
    this.defaultObject = Optional.of(defaultObject);
  }

  public FlexibleClassRenderer(Function<Object, String> getStringRepresentation) {
    this.getStringRepresentation = getStringRepresentation;
    this.defaultObject = Optional.empty();
  }

  @Override
  protected void setValue(Object value) {
    this.setText(getStringRepresentation.apply(value));
  }

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    var component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    if (defaultObject.isPresent() && defaultObject.equals(value)) {
      component.setForeground(Color.GRAY);
    }
    return component;
  }
}
