// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class ComboBoxInputField<E extends Enum<E>> implements InputField<E> {
  private final String label;
  private final String name;
  private final Class<E> enumType;
  private JComboBox<E> comboBox;
  private final E defaultValue;

  public ComboBoxInputField(String label, String name, Class<E> enumType, E defaultValue) {
    this.label = label;
    this.name = name;
    this.enumType = enumType;
    this.defaultValue = defaultValue;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    comboBox = new JComboBox<>(enumType.getEnumConstants());
    comboBox.setName("InputFieldComboBox" + name);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(comboBox);
  }

  @Override
  public E getUserInput() {
    return comboBox.getItemAt(comboBox.getSelectedIndex());
  }

  @Override
  public void resetField() {
    comboBox.setSelectedItem(defaultValue);
  }
}
