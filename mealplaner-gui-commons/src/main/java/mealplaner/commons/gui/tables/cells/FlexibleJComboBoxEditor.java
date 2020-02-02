// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.tables.cells;

import java.awt.Component;
import java.util.function.BiConsumer;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class FlexibleJComboBoxEditor<T> extends AbstractCellEditor implements TableCellEditor {
  private JComboBox<T> editor;
  private final int columnToGetValues;
  private transient BiConsumer<DefaultComboBoxModel<T>, Object> addValuesToModel;

  public FlexibleJComboBoxEditor(int columnToGetValues, BiConsumer<DefaultComboBoxModel<T>, Object> addValuesToModel) {
    this.columnToGetValues = columnToGetValues;
    this.addValuesToModel = addValuesToModel;
    editor = new JComboBox<>();
  }

  @Override
  public Object getCellEditorValue() {
    return editor.getSelectedItem();
  }


  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    var columnValue = table.getValueAt(row, columnToGetValues);
    var comboBoxModel = new DefaultComboBoxModel<T>();

    addValuesToModel.accept(comboBoxModel, columnValue);

    editor.setModel(comboBoxModel);
    editor.setSelectedItem(value);
    return editor;
  }
}
