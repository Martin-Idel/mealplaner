package mealplaner.gui.commons;

import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;

public class SwingUtilityMethods {

	public static JButton createButton(String label, String mnemonic, ActionListener listener) {
		JButton button = new JButton(label);
		button.setMnemonic(KeyStroke.getKeyStroke(mnemonic).getKeyCode());
		button.addActionListener(listener);
		return button;
	}

	public static <E extends Enum<E>> void setupComboBoxEditor(TableColumn column,
			Class<E> enumType) {
		JComboBox<E> comboBox = new JComboBox<E>(enumType.getEnumConstants());
		column.setCellEditor(new DefaultCellEditor(comboBox));
	}
}
