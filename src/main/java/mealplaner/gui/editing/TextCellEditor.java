package mealplaner.gui.editing;
/**
 * Martin Idel, 02.02.2016 (last update)
 * TextCellEditor: for Datenbankbearbeitung: Change text field
 * "Name" - must not be empty or only Whitespace.
 * This was inspired by something, but I don't know the address anymore (years back).
 **/

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class TextCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private static final Border red = new LineBorder(Color.RED);
	private static final Border black = new LineBorder(Color.BLACK);
	private JTextField textField;

	public TextCellEditor() {
		super(new JTextField());
		textField = (JTextField) getComponent();
		textField.setHorizontalAlignment(JTextField.LEFT);
	}

	@Override
	public boolean stopCellEditing() {
		try {
			String v = textField.getText();
			if (v.trim().isEmpty()) {
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
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		textField.setBorder(black);
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}