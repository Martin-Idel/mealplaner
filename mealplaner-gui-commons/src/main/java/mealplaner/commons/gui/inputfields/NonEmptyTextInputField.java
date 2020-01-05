// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import javax.swing.JLabel;
import javax.swing.JTextField;

import mealplaner.commons.gui.GuiPanel;

public class NonEmptyTextInputField implements InputField<Optional<String>> {
  private final String label;
  private final String name;
  private JTextField nonEmptyTextField;
  private final int orderNumber;

  public NonEmptyTextInputField(String label, String name, int orderNumber) {
    this.label = label;
    this.name = name;
    this.orderNumber = orderNumber;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    nonEmptyTextField = new JTextField();
    nonEmptyTextField.setName("InputFieldNonemptyText" + name);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(nonEmptyTextField);
  }

  @Override
  public Optional<String> getUserInput() {
    String trimmedUserInput = nonEmptyTextField.getText().trim();
    return trimmedUserInput.isEmpty() ? empty() : of(trimmedUserInput);
  }

  @Override
  public void resetField() {
    nonEmptyTextField.setText("");
  }

  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
