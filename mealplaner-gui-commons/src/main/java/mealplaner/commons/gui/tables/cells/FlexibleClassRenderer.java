// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.cells;

import java.util.function.Function;
import javax.swing.table.DefaultTableCellRenderer;

public class FlexibleClassRenderer extends DefaultTableCellRenderer {
  private transient Function<Object, String> getStringRepresentation;

  public FlexibleClassRenderer(Function<Object, String> getStringRepresentation) {
    this.getStringRepresentation = getStringRepresentation;
  }

  protected void setValue(Object value) {
    this.setText(getStringRepresentation.apply(value));
  }
}
