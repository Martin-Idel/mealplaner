package mealplaner.commons.gui.inputfields;

import javax.swing.JLabel;
import javax.swing.JTextField;

import mealplaner.commons.gui.GuiPanel;

public class TextInputField implements InputField<String> {
  private final String label;
  private JTextField textField;

  public TextInputField(String label) {
    this.label = label;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    textField = new JTextField();
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(textField);
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
