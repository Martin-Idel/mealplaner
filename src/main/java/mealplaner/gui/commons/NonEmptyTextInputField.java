package mealplaner.gui.commons;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NonEmptyTextInputField implements InputField<Optional<String>> {
	private String label;
	private JTextField nonEmptyTextField;

	public NonEmptyTextInputField(String label) {
		this.label = label;
	}

	@Override
	public void addToPanel(JPanel panel) {
		nonEmptyTextField = new JTextField();
		panel.add(new JLabel(label));
		panel.add(nonEmptyTextField);
	}

	@Override
	public Optional<String> getUserInput() {
		String trimmedUserInput = nonEmptyTextField.getText().trim();
		return trimmedUserInput.isEmpty() ? empty() : of(trimmedUserInput);
	}

	@Override
	public void resetField() {
		nonEmptyTextField.setText("");
	}
}
