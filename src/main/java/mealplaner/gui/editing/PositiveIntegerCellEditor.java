package mealplaner.gui.editing;
/**
 * Martin Idel,
 * TextCellEditor: for Datenbankbearbeitung: based on TextCellEditor. Allows only nonnegative numbers to be entered.
 * Works differently than PositiveTextFieldEditor.
 * "tageHer" must be a positive number.
 * Same inspiration as TextCellEditor.
 **/

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class PositiveIntegerCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private static final Border red = new LineBorder(Color.RED);
	private static final Border black = new LineBorder(Color.BLACK);
	private JTextField textField;

	public PositiveIntegerCellEditor() {
		super(new JTextField());
		textField = (JTextField) getComponent();
		textField.setHorizontalAlignment(JTextField.RIGHT);
	}

	@Override
	public boolean stopCellEditing() {
		try {
			int v = Integer.valueOf(textField.getText());
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
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		textField.setBorder(black);
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}