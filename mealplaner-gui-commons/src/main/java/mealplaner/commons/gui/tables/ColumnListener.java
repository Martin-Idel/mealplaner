// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.IntConsumer;
import javax.swing.JTable;

public final class ColumnListener extends MouseAdapter {
  private int columnNumber;
  private final IntConsumer onClick;

  private ColumnListener(int columnNumber, IntConsumer onClick) {
    this.columnNumber = columnNumber;
    this.onClick = onClick;
  }

  public static ColumnListener createColumnListener(int columnNumber, IntConsumer onClick) {
    return new ColumnListener(columnNumber, onClick);
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public void setColumnNumber(int columnNumber) {
    this.columnNumber = columnNumber;
  }

  @Override
  public void mouseClicked(MouseEvent event) {
    JTable tableSource = (JTable) event.getSource();
    int row = tableSource.getSelectedRow();
    int column = tableSource.getSelectedColumn();
    if (column == columnNumber) {
      onClick.accept(row);
    }
  }
}
