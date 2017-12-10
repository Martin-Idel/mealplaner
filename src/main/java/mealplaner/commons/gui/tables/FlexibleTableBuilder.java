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

public class FlexibleTableBuilder {
  private List<TableColumnData<?>> columns;
  private List<MouseAdapter> columnListeners;
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

  public static FlexibleTableBuilder createNewTable() {
    return new FlexibleTableBuilder();
  }

  public FlexibleTableBuilder addColumn(TableColumnData<?> column) {
    columns.add(column);
    return this;
  }

  public FlexibleTableBuilder withRowCount(Supplier<Integer> rowCount) {
    this.rowCount = rowCount;
    return this;
  }

  public FlexibleTableBuilder addListenerToThisColumn(Consumer<Integer> onClick) {
    columnListeners.add(ColumnListener.createColumnListener(columns.size() - 1, onClick));
    return this;
  }

  public FlexibleTableBuilder addDefaultRowToUnderlyingModel(Runnable addRow) {
    this.addRow = addRow;
    return this;
  }

  public FlexibleTableBuilder deleteRowsOnDelete(Consumer<Integer> deleteRows) {
    this.deleteRow = deleteRows;
    return this;
  }

  public Table buildTable() {
    UpdateSizeTableModel tableModel = UpdateSizeTableModel.from(columns, rowCount);
    table = new JTable(tableModel);

    setGuiAppearanceForColumns();
    setColumnListenersIfNecessary();
    return Table.from(tableModel, table);
  }

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
    actionMap.put("deleteRow", new DeleteAction(tableModel));
  }

  private TableColumn getTableColumn(int index) {
    return table.getColumnModel().getColumn(index);
  }

  class DeleteAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private FlexibleTableModel tableModel;

    DeleteAction(FlexibleTableModel tableModel) {
      this.tableModel = tableModel;
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
