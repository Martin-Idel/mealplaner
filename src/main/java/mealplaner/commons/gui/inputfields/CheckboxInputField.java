package mealplaner.commons.gui.inputfields;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class CheckboxInputField implements InputField<Boolean> {
  private final String name;
  private JCheckBox checkBox;

  public CheckboxInputField(String name) {
    this.name = name;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    checkBox = new JCheckBox();
    panel.getComponent().add(new JLabel(name));
    panel.getComponent().add(checkBox);
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
