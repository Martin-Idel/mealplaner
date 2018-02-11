package mealplaner.commons.gui.inputfields;

import javax.swing.JLabel;
import javax.swing.JTextField;

import mealplaner.commons.gui.GuiPanel;

public class TextInputField implements InputField<String> {
  private final String label;
  private final String name;
  private JTextField textField;

  public TextInputField(String label, String name) {
    this.label = label;
    this.name = name;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    textField = new JTextField();
    textField.setName("InputFieldText" + name);
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
