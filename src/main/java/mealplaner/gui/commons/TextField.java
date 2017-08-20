package mealplaner.gui.commons;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextField implements InputField<String> {
	private String label;
	private JTextField textField;

	public TextField(String label) {
		this.label = label;
	}

	@Override
	public void addToPanel(JPanel panel) {
		textField = new JTextField();
		panel.add(new JLabel(label));
		panel.add(textField);
	}

	@Override
	public String getUserInput() {
		String trimmedUserInput = textField.getText().trim();
		return trimmedUserInput;
	}

	@Override
	public void resetField() {
		textField.setText("");
	}
}
