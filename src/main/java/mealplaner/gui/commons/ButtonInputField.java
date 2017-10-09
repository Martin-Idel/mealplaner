package mealplaner.gui.commons;

import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonInputField<T> implements InputField<T> {
	private JButton button;
	private String label;
	private String buttonLabel;
	private String buttonLabelForDefaultContent;
	private T content;
	private T defaultContent;
	private Supplier<T> contentSupplier;

	public ButtonInputField(String label, String buttonLabel, String buttonLabelForDefaultContent,
			T defaultContent, Supplier<T> contentSupplier) {
		this.label = label;
		this.buttonLabel = buttonLabel;
		this.buttonLabelForDefaultContent = buttonLabelForDefaultContent;
		this.content = defaultContent;
		this.defaultContent = defaultContent;
		this.contentSupplier = contentSupplier;
	}

	@Override
	public void addToPanel(JPanel panel) {
		button = new JButton(
				content.equals(defaultContent) ? buttonLabelForDefaultContent : buttonLabel);
		button.addActionListener(action -> {
			content = contentSupplier.get();
			button.setText(content.equals(defaultContent)
					? buttonLabelForDefaultContent
					: buttonLabel);
		});
		panel.add(new JLabel(label));
		panel.add(button);
	}

	@Override
	public T getUserInput() {
		return content;
	}

	@Override
	public void resetField() {
		content = defaultContent;
		button.setText(buttonLabelForDefaultContent);
	}
}
