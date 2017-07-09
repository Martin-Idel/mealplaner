package mealplaner.gui.commons;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.editing.NonnegativeIntegerTextFilter;

public class NonnegativeIntegerInputField implements InputField<NonnegativeInteger> {
	private String label;
	private JTextField nonnegativeIntegerTextField;
	private NonnegativeInteger defaultValue;

	public NonnegativeIntegerInputField(String label, NonnegativeInteger defaultValue) {
		this.label = label;
		this.defaultValue = defaultValue;
	}

	@Override
	public void addToPanel(JPanel panel) {
		nonnegativeIntegerTextField = new JTextField(defaultValue.toString());
		PlainDocument doc = (PlainDocument) nonnegativeIntegerTextField.getDocument();
		doc.setDocumentFilter(new NonnegativeIntegerTextFilter());
		nonnegativeIntegerTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent ev) {
				nonnegativeIntegerTextField.selectAll();
			}

			@Override
			public void focusLost(FocusEvent ev) {
			}
		});
		panel.add(new JLabel(label));
		panel.add(nonnegativeIntegerTextField);
	}

	@Override
	public NonnegativeInteger getUserInput() {
		return new NonnegativeInteger(Integer.parseInt(nonnegativeIntegerTextField.getText()));
	}

	@Override
	public void resetField() {
		nonnegativeIntegerTextField.setText(defaultValue.toString());
	}
}