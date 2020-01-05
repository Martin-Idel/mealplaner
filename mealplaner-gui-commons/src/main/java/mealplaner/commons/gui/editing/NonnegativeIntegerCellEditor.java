// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.editing;

import static java.lang.Integer.parseInt;
import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import mealplaner.commons.NonnegativeInteger;

public class NonnegativeIntegerCellEditor extends DefaultCellEditor {
  private static final long serialVersionUID = 1L;
  private static final Border red = new LineBorder(Color.RED);
  private static final Border black = new LineBorder(Color.BLACK);
  private final JTextField textField;

  public NonnegativeIntegerCellEditor() {
    super(new JTextField());
    textField = (JTextField) getComponent();
    textField.setHorizontalAlignment(JTextField.RIGHT);
  }

  @Override
  public boolean stopCellEditing() {
    try {
      int v = Integer.parseInt(textField.getText());
      if (v < 0) {
        textField.setBorder(red);
        return false;
      }
    } catch (NumberFormatException e) {
      textField.setBorder(red);
      return false;
    }
    return super.stopCellEditing();
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
      int row, int column) {
    textField.setBorder(black);
    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
  }

  @Override
  public NonnegativeInteger getCellEditorValue() {
    return nonNegative(parseInt((String) super.getCellEditorValue()));
  }
}