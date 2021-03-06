// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.editing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class NonemptyTextCellEditor extends DefaultCellEditor {
  private static final long serialVersionUID = 1L;
  private static final Border red = new LineBorder(Color.RED);
  private static final Border black = new LineBorder(Color.BLACK);
  private final JTextField textField;

  public NonemptyTextCellEditor() {
    super(new JTextField());
    textField = (JTextField) getComponent();
    textField.setHorizontalAlignment(SwingConstants.LEFT);
  }

  @Override
  public boolean stopCellEditing() {
    try {
      String v = textField.getText();
      if (checkTrimEmpty(v)) {
        textField.setBorder(red);
        return false;
      }
    } catch (IllegalArgumentException e) {
      textField.setBorder(red);
      return false;
    }
    return super.stopCellEditing();
  }

  @Override
  public Component getTableCellEditorComponent(
      JTable table, Object value, boolean isSelected, int row, int column) {
    textField.setBorder(black);
    return super.getTableCellEditorComponent(table, value, isSelected, row, column);
  }

  private boolean checkTrimEmpty(String str) {
    for (int i = 0; i < str.length(); i++) {
      if (!Character.isWhitespace(str.charAt(i))) {
        return false;
      }
    }
    return true;
  }
}