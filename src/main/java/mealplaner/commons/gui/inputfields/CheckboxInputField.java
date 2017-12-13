package mealplaner.commons.gui.inputfields;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckboxInputField implements InputField<Boolean> {
  private final String name;
  private JCheckBox checkBox;

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
