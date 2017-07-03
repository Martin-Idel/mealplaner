package mealplaner.gui.commons;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckboxInputField implements InputField<Boolean> {
	private JCheckBox checkBox;
	private String name;

	public CheckboxInputField(String name) {
		this.name = name;
	}

	@Override
	public void addToPanel(JPanel panel) {
		checkBox = new JCheckBox();
		panel.add(new JLabel(name));
		panel.add(checkBox);
	}

	@Override
	public Boolean getUserInput() {
		return checkBox.isSelected();
	}

	@Override
	public void resetField() {
		checkBox.setSelected(false);
	}

}
