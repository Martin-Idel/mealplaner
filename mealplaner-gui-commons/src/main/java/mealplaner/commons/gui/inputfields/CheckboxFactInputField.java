// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class CheckboxFactInputField<T> implements InputField<T> {
  private final String label;
  private final String name;
  private JCheckBox checkBox;
  private final int orderNumber;
  private final Function<Boolean, T> conversion;

  public CheckboxFactInputField(
      String label,
      String name,
      int orderNumber,
      Function<Boolean, T> conversion) {
    this.label = label;
    this.name = name;
    this.orderNumber = orderNumber;
    this.conversion = conversion;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    checkBox = new JCheckBox();
    checkBox.setName("InputFieldCheckBox" + name);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(checkBox);
  }

  @Override
  public T getUserInput() {
    return conversion.apply(checkBox.isSelected());
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
