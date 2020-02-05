// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.models;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.Optional;
import java.util.function.IntSupplier;
import javax.swing.table.AbstractTableModel;

/**
 * This class provides the base class of the table model to be built by the
 * FlexibleTableBuilder. The real types are subtypes of this class. It holds the
 * columns build using FlexibleTableBuilder. Clients still need to provide
 * mechanisms to set and get values from a data structure.
 */
public abstract class FlexibleTableModel extends AbstractTableModel {
  private static final long serialVersionUID = 1L;

  final transient List<TableColumnData<?>> columns;

  private final transient IntSupplier rowCount;

  FlexibleTableModel(List<TableColumnData<?>> tableColumns, IntSupplier rowCount) {
    columns = tableColumns;
    this.rowCount = rowCount;
  }

  @Override
  public int getColumnCount() {
    return columns.size();
  }

  @Override
  public int getRowCount() {
    return rowCount.getAsInt();
  }

  @Override
  public String getColumnName(int col) {
    return columns.get(col).columnName();
  }

  @Override
  public Class<?> getColumnClass(int col) {
    return columns.get(col).getColumnClass();
  }

  @Override
  public boolean isCellEditable(int row, int col) {
    return columns.get(col).isEditable(row);
  }

  public void insertRows(int firstRow, int lastRow) {
    fireTableRowsInserted(firstRow, lastRow);
  }

  public void deleteRows(int firstRow, int lastRow) {
    fireTableRowsDeleted(firstRow, lastRow);
  }

  public void updateTable() {
    fireTableDataChanged();
  }

  void usualSetValueAt(Object value, int row, int col) {
    Optional<Integer[]> fireOtherCellsUpdated = columns.get(col).setValue(value, row);
    fireTableCellUpdated(row, col);
    fireOtherCellsUpdated.ifPresent(otherColumns -> asList(otherColumns)
        .forEach(column -> fireTableCellUpdated(row, column)));
  }
}
