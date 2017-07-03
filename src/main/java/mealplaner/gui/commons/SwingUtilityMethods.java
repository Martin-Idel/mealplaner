package mealplaner.gui.commons;

import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.ResourceBundle;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.KeyStroke;
import javax.swing.table.TableColumn;

import mealplaner.gui.editing.EnumListCellRenderer;
import mealplaner.gui.editing.EnumListTableCellRenderer;

public class SwingUtilityMethods {

	public static JButton createButton(ResourceBundle messages, String label, String mnemonic,
			ActionListener listener) {
		JButton button = new JButton(messages.getString(label));
		button.setMnemonic(KeyStroke.getKeyStroke(messages.getString(mnemonic)).getKeyCode());
		button.addActionListener(listener);
		return button;
	}

	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> void setupComboBoxEditor(TableColumn column, Class<E> enumType,
			EnumMap<E, String> enumList) {
		JComboBox<E> comboBox = new JComboBox<E>(enumType.getEnumConstants());
		comboBox.setRenderer(new EnumListCellRenderer<E>(enumList));
		column.setCellEditor(new DefaultCellEditor(comboBox));
	}

	public static <E extends Enum<E>> void setupEnumColumnRenderer(TableColumn column, Class<E> enumType,
			EnumMap<E, String> enumList) {
		column.setCellRenderer(new EnumListTableCellRenderer<E>(enumList));
	}

}
