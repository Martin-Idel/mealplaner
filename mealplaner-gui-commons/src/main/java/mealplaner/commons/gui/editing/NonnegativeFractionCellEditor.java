// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.editing;

import static mealplaner.commons.NonnegativeFraction.parse;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import mealplaner.commons.NonnegativeFraction;

public class NonnegativeFractionCellEditor extends DefaultCellEditor {

  private static final long serialVersionUID = 1L;
  private static final Border red = new LineBorder(Color.RED);
  private static final Border black = new LineBorder(Color.BLACK);
  private final JTextField textField;

  public NonnegativeFractionCellEditor() {
    super(new JTextField());
    textField = (JTextField) getComponent();
    textField.setHorizontalAlignment(SwingConstants.RIGHT);
  }

  @Override
  public boolean stopCellEditing() {
    try {
      parse(textField.getText());
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
  public NonnegativeFraction getCellEditorValue() {
    return parse((String) super.getCellEditorValue());
  }
}
