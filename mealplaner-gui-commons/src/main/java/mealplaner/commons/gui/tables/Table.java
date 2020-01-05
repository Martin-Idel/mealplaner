// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.commons.gui.GuiComponent;
import mealplaner.commons.gui.tables.models.FlexibleTableModel;

/**
 * This class provides an abstraction over a Swing Table containing a
 * FlexibleTableModel. Tables can be build using the FlexibleTableBuilder.
 */
public final class Table implements GuiComponent {
  private final FlexibleTableModel tableModel;
  private final JTable table;

  private Table(FlexibleTableModel tableModel, JTable table) {
    this.tableModel = tableModel;
    this.table = table;
  }

  public static Table from(FlexibleTableModel tableModel, JTable table) {
    return new Table(tableModel, table);
  }

  @Override
  public JScrollPane getComponent() {
    return new JScrollPane(table);
  }

  public JTable getTable() {
    return table;
  }

  /**
   * Print the table.
   * 
   * @param frame
   *          The parent component of the printer message dialog. Can be null.
   */
  public void printTable(Component frame) {
    TablePrinter.printTable(table, frame);
  }

  /**
   * Update the table. This function must be called when updating the underlying
   * data structure programatically such that changes to the data are reflected to
   * the user.
   */
  public void update() {
    tableModel.updateTable();
  }

  /**
   * Insert rows between firstRow and lastRow.
   */
  public void insertRows(int firstRow, int lastRow) {
    tableModel.insertRows(firstRow, lastRow);
  }

  /**
   * Delete rows between firstRow and lastRow.
   */
  public void deleteRows(int firstRow, int lastRow) {
    tableModel.deleteRows(firstRow, lastRow);
  }

  /**
   * Get all selected rows in the column. This might be useful to delete selected
   * rows.
   */
  public int[] getSelectedRows() {
    return table.getSelectedRows();
  }
}
