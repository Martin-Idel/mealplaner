package mealplaner.commons.gui.tables;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import mealplaner.commons.gui.GuiComponent;
import mealplaner.commons.gui.tables.models.FlexibleTableModel;

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

  public void printTable(Component frame) {
    TablePrinter.printTable(table, frame);
  }

  public void update() {
    tableModel.updateTable();
  }

  public void insertRows(int firstRow, int lastRow) {
    tableModel.insertRows(firstRow, lastRow);
  }

  public void deleteRows(int firstRow, int lastRow) {
    tableModel.deleteRows(firstRow, lastRow);
  }

  public int[] getSelectedRows() {
    return table.getSelectedRows();
  }
}
