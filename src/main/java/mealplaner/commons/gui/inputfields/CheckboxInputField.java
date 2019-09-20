// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class CheckboxInputField implements InputField<Boolean> {
  private final String label;
  private final String name;
  private JCheckBox checkBox;
  private final int orderNumber;

  public CheckboxInputField(String label, String name, int orderNumber) {
    this.label = label;
    this.name = name;
    this.orderNumber = orderNumber;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    checkBox = new JCheckBox();
    checkBox.setName("InputFieldCheckBox" + name);
    panel.getComponent().add(new JLabel(label));
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


  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
