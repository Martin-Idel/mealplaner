// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import java.util.List;
import java.util.function.Supplier;

/**
 * This class provides a table model to be built by the FlexibleTableBuilder. It
 * allows to have a table with an arbitrary amount of columns and rows, backed
 * by an arbitrary data structure which allows the user to enter new entries by
 * calling and update function
 */
public final class UpdateSizeTableModel extends FlexibleTableModel {
  private static final long serialVersionUID = 1L;

  private UpdateSizeTableModel(List<TableColumnData<?>> tableColumns,
                               Supplier<Integer> rowCount) {
    super(tableColumns, rowCount);
  }

  public static UpdateSizeTableModel from(List<TableColumnData<?>> tableColumns,
      Supplier<Integer> rowCount) {
    return new UpdateSizeTableModel(tableColumns, rowCount);
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    usualSetValueAt(value, row, col);
  }

  @Override
  public Object getValueAt(int row, int col) {
    return columns.get(col).getValue(row);
  }
}
