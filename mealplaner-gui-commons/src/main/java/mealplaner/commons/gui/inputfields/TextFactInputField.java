// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mealplaner.commons.gui.GuiPanel;

public class TextFactInputField<T> implements InputField<T> {
  private final String label;
  private final String name;
  private JTextField textField;
  private final int orderNumber;
  private final Function<String, T> conversion;

  public TextFactInputField(String label, String name, int orderNumber, Function<String, T> conversion) {
    this.label = label;
    this.name = name;
    this.orderNumber = orderNumber;
    this.conversion = conversion;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    textField = new JTextField();
    textField.setName("InputFieldText" + name);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(textField);
  }

  @Override
  public T getUserInput() {
    return conversion.apply(textField.getText().trim());
  }

  @Override
  public void resetField() {
    textField.setText("");
  }

  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
