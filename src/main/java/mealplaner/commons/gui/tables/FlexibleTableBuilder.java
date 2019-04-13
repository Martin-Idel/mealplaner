// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables;

import static java.util.Arrays.stream;
import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;

import mealplaner.commons.gui.tables.models.DynamicSizeTableModel;
import mealplaner.commons.gui.tables.models.FlexibleTableModel;
import mealplaner.commons.gui.tables.models.TableColumnData;
import mealplaner.commons.gui.tables.models.UpdateSizeTableModel;

/**
 * The FlexibleTableBuilder provides a fluent API to build Swing table models
 * which are type safe. Columns can be added using the TableColumnBuilder.
 * Resulting tables are somewhat standardized, e.g. any column can only contain
 * one type of data.
 */
public final class FlexibleTableBuilder {
  private final List<TableColumnData<?>> columns;
  private final List<MouseAdapter> columnListeners;
  private JTable table;
  private Supplier<Integer> rowCount = () -> 0;
  private Runnable addRow = () -> {
  };
  private Consumer<Integer> deleteRow = number -> {
  };

  private FlexibleTableBuilder() {
    columns = new ArrayList<>();
    columnListeners = new ArrayList<>();
  }

  /**
   * Obtain a Builder of TableModels
   * 
   * @return a new instance of a builder
   */
  public static FlexibleTableBuilder createNewTable() {
    return new FlexibleTableBuilder();
  }

  /**
   * Add a new Column to this table model. The column should be build using
   * TableColumnBuilder
   * 
   * @param column
   *          to be added
   * @return the current builder
   */
  public FlexibleTableBuilder addColumn(TableColumnData<?> column) {
    columns.add(column);
    return this;
  }

  /**
   * Provide a way to query the table for the number of rows. If you use a list as
   * base of your table model, this would just be a call to List::size(). It is
   * imperative your data structure can return (and/or set) as many values as the
   * rowCount seems to have.
   * 
   * @param rowCount
   *          supplier of number of rows of your table (e.g. List::size() if you
   *          work with lists)
   * @return the current builder
   */
  public FlexibleTableBuilder withRowCount(Supplier<Integer> rowCount) {
    this.rowCount = rowCount;
    return this;
  }

  /**
   * Add a listener to the last column added. The listener takes a row number and
   * performs some action on your underlying data structure. This can be used to
   * e.g. open dialogs when clicking on cells.
   * 
   * @param onClick
   *          Listener: Get a row number, perform an action on the cell designated
   *          by that row number.
   * @return the current builder
   */
  public FlexibleTableBuilder addListenerToThisColumn(Consumer<Integer> onClick) {
    columnListeners.add(ColumnListener.createColumnListener(columns.size() - 1, onClick));
    return this;
  }

  /**
   * This function has only any effect when creating a dynamic size table. In that
   * case, the last row of your table will always be an "empty" row that when
   * edited enlarges the table. Therefore, you need to provide a function which
   * adds an "empty" row to your underlying data structure.
   * 
   * @param addRow
   *          Provide a function adding an empty row to your data structure (i.e.
   *          add a default entry to your list if you use a list as data
   *          structure)
   * @return the current builder
   */
  public FlexibleTableBuilder addDefaultRowToUnderlyingModel(Runnable addRow) {
    this.addRow = addRow;
    return this;
  }

  /**
   * Lets the user delete rows from the table by selecting them and pressing the
   * delete button. To do so, the user must provide a function which upon getting
   * a row value will delete the corresponding data from the underlying data
   * structure (e.g. call List::remove() when using a list as a data structure).
   * 
   * @param deleteRows
   *          Function which deletes a row with given number from the underlying
   *          data structure
   * @return the current builder
   */
  public FlexibleTableBuilder deleteRowsOnDelete(Consumer<Integer> deleteRows) {
    this.deleteRow = deleteRows;
    return this;
  }

  /**
   * Using all of columns and other aspects, build a table (UpdateSizeTableModel)
   * 
   * @return new Table backed by the columns entered in this builder. Keep in mind
   *         that the data structure is not stored in the table, the table only
   *         provides a view on the data.
   */
  public Table buildTable() {
    UpdateSizeTableModel tableModel = UpdateSizeTableModel.from(columns, rowCount);
    table = new JTable(tableModel);

    setGuiAppearanceForColumns();
    setColumnListenersIfNecessary();
    return Table.from(tableModel, table);
  }

  /**
   * Using all of columns and other aspects, build a table (DynamicSizeTableModel)
   * 
   * @return new Table backed by the columns entered in this builder. Make sure
   *         that you called "addDefaultRowToUnderlyingModel" before. Keep in mind
   *         that the data structure is not stored in the table, the table only
   *         provides a view on the data.
   */
  public Table buildDynamicTable() {
    DynamicSizeTableModel tableModel = DynamicSizeTableModel.from(columns, rowCount, addRow);
    table = new JTable(tableModel);

    setGuiAppearanceForColumns();
    setColumnListenersIfNecessary();
    enableDeletionOfLines(tableModel);
    return Table.from(tableModel, table);
  }

  private void setColumnListenersIfNecessary() {
    for (MouseAdapter columnListener : columnListeners) {
      table.addMouseListener(columnListener);
    }
  }

  private void setGuiAppearanceForColumns() {
    for (int i = 0; i < columns.size(); i++) {
      TableColumn column = getTableColumn(i);
      column.setPreferredWidth(columns.get(i).getPreferredSize());
      columns.get(i).getTableCellEditor().ifPresent(column::setCellEditor);
      columns.get(i).getTableCellRenderer().ifPresent(column::setCellRenderer);
    }
  }

  private void enableDeletionOfLines(FlexibleTableModel tableModel) {
    InputMap inputMap = table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = table.getActionMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteRow");
    actionMap.put("deleteRow", new DeleteAction(tableModel, table, deleteRow));
  }

  private TableColumn getTableColumn(int index) {
    return table.getColumnModel().getColumn(index);
  }

  static class DeleteAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private final FlexibleTableModel tableModel;
    private final JTable table;
    private final Consumer<Integer> deleteRow;

    DeleteAction(FlexibleTableModel tableModel, JTable table, Consumer<Integer> deleteRow) {
      this.tableModel = tableModel;
      this.table = table;
      this.deleteRow = deleteRow;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      int[] selectedRows = table.getSelectedRows();
      stream(selectedRows).boxed()
          .sorted(reverseOrder())
          .collect(toList())
          .forEach(row -> {
            deleteRow.accept(row);
            tableModel.deleteRows(row, row);
          });
    }
  }
}
