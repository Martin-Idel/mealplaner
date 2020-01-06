// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import java.util.List;
import java.util.function.Supplier;

/**
 * This class provides a table model to be built by the FlexibleTableBuilder. It
 * allows to have a table with an arbitrary amount of columns and rows, backed
 * by an arbitrary data structure which allows the user to enter new entries by
 * editing the last (empty) row.
 *
 */
public final class DynamicSizeTableModel extends FlexibleTableModel {
  private static final long serialVersionUID = 1L;
  private final transient Runnable addValue;

  private DynamicSizeTableModel(List<TableColumnData<?>> tableColumns,
                                Supplier<Integer> rowCount,
                                Runnable addValue) {
    super(tableColumns, rowCount);
    this.addValue = addValue;
  }

  public static DynamicSizeTableModel from(List<TableColumnData<?>> tableColumns,
      Supplier<Integer> rowCount, Runnable addValue) {
    return new DynamicSizeTableModel(tableColumns, rowCount, addValue);
  }

  @Override
  public int getRowCount() {
    return super.getRowCount() + 1;
  }

  @Override
  public void setValueAt(Object value, int row, int col) {
    if (row == getRowCount() - 1) {
      addValue.run();
      insertRows(getRowCount(), getRowCount());
      setValueAt(value, row, col);
    } else {
      usualSetValueAt(value, row, col);
    }
  }

  @Override
  public Object getValueAt(int row, int col) {
    return (row == getRowCount() - 1)
        ? columns.get(col).getDefaultValue()
        : columns.get(col).getValue(row);
  }
}
