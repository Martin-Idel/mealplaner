package mealplaner.gui.commons;

import java.util.EnumMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mealplaner.gui.editing.EnumListCellRenderer;

public class ComboBoxInputField<E extends Enum<E>> implements InputField<E> {
	private String label;
	private Class<E> enumType;
	private EnumMap<E, String> enumList;
	private JComboBox<E> comboBox;
	private E defaultValue;

	public ComboBoxInputField(String label, Class<E> enumType, EnumMap<E, String> enumList, E defaultValue) {
		this.label = label;
		this.enumType = enumType;
		this.enumList = enumList;
		this.defaultValue = defaultValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void addToPanel(JPanel panel) {
		comboBox = new JComboBox<E>(enumType.getEnumConstants());
		comboBox.setRenderer(new EnumListCellRenderer<E>(enumList));
		panel.add(new JLabel(label));
		panel.add(comboBox);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E getUserInput() {
		return (E) comboBox.getSelectedItem();
	}

	@Override
	public void resetField() {
		comboBox.setSelectedItem(defaultValue);
	}
}
