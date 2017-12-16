package mealplaner.commons.gui.inputfields;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextInputField implements InputField<String> {
  private final String label;
  private JTextField textField;

  public TextInputField(String label) {
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
    return textField.getText().trim();
  }

  @Override
  public void resetField() {
    textField.setText("");
  }
}