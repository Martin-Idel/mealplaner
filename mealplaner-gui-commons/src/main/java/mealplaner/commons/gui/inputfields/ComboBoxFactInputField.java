// SPDX-License-Identifier: MIT

package mealplaner.commons.gui.inputfields;

import java.util.function.Function;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class ComboBoxFactInputField<E extends Enum<E>, T> implements InputField<T> {
  private final String label;
  private final String name;
  private final Class<E> enumType;
  private JComboBox<E> comboBox;
  private final E defaultValue;
  private final int orderNumber;
  private final Function<E, T> conversion;

  public ComboBoxFactInputField(
      String label,
      String name,
      Class<E> enumType,
      E defaultValue,
      int orderNumber,
      Function<E, T> conversion) {
    this.label = label;
    this.name = name;
    this.enumType = enumType;
    this.defaultValue = defaultValue;
    this.orderNumber = orderNumber;
    this.conversion = conversion;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    comboBox = new JComboBox<>(enumType.getEnumConstants());
    comboBox.setName("InputFieldComboBox" + name);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(comboBox);
  }

  @Override
  public T getUserInput() {
    return conversion.apply(comboBox.getItemAt(comboBox.getSelectedIndex()));
  }

  @Override
  public void resetField() {
    comboBox.setSelectedItem(defaultValue);
  }

  @Override
  public int getOrdering() {
    return orderNumber;
  }
}
