package mealplaner.gui.dialogs.recepies;

import java.awt.Component;
import java.util.Arrays;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.Measure;

// TODO: Make generic
public class FlexibleJComboBoxEditor extends AbstractCellEditor implements TableCellEditor {
  private JComboBox<Measure> editor;
  private final int columnToGetValues;

  public FlexibleJComboBoxEditor(int columnToGetValues) {
    editor = new JComboBox<>();
    this.columnToGetValues = columnToGetValues;
  }

  @Override
  public Object getCellEditorValue() {
    return editor.getSelectedItem();
  }


  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
    var ingredient = table.getValueAt(row, columnToGetValues);
    var comboBoxModel = new DefaultComboBoxModel<Measure>();
    if (ingredient instanceof Ingredient) {
      var secondaries = ((Ingredient) ingredient).getMeasures().getSecondaries();
      comboBoxModel.addAll(secondaries.keySet());
      comboBoxModel.addElement(((Ingredient) ingredient).getPrimaryMeasure());
    } else {
      comboBoxModel.addAll(Arrays.asList(Measure.values()));
    }

    editor.setModel(comboBoxModel);
    editor.setSelectedItem(value);
    return editor;
  }
}
