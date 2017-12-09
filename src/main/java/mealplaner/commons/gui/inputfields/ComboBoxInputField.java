package mealplaner.commons.gui.inputfields;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ComboBoxInputField<E extends Enum<E>> implements InputField<E> {
	private String label;
	private Class<E> enumType;
	private JComboBox<E> comboBox;
	private E defaultValue;

	public ComboBoxInputField(String label, Class<E> enumType, E defaultValue) {
		this.label = label;
		this.enumType = enumType;
		this.defaultValue = defaultValue;
	}

	@Override
	public void addToPanel(JPanel panel) {
		comboBox = new JComboBox<E>(enumType.getEnumConstants());
		panel.add(new JLabel(label));
		panel.add(comboBox);
	}

	@Override
	public E getUserInput() {
		return comboBox.getItemAt(comboBox.getSelectedIndex());
	}

	@Override
	public void resetField() {
		comboBox.setSelectedItem(defaultValue);
	}
}
