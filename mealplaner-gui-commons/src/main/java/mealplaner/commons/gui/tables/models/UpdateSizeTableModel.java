// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import java.util.List;
import java.util.function.IntSupplier;

/**
 * This class provides a table model to be built by the FlexibleTableBuilder. It
 * allows to have a table with an arbitrary amount of columns and rows, backed
 * by an arbitrary data structure which allows the user to enter new entries by
 * calling and update function
 */
public final class UpdateSizeTableModel extends FlexibleTableModel {
  private static final long serialVersionUID = 1L;

  private UpdateSizeTableModel(
      List<TableColumnData<?>> tableColumns, IntSupplier rowCount) {
    super(tableColumns, rowCount);
  }

  public static UpdateSizeTableModel from(
      List<TableColumnData<?>> tableColumns, IntSupplier rowCount) {
    return new UpdateSizeTableModel(tableColumns, rowCount);
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    if (value != null) {
      usualSetValueAt(value, row, col);
    }
  }

  @Override
  public Object getValueAt(int row, int col) {
    return (row - 1 == getRowCount()) ? "" : columns.get(col).getValue(row);
  }
}
