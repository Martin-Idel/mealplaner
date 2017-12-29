package mealplaner.commons.gui.inputfields;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;

public class ComboBoxInputField<E extends Enum<E>> implements InputField<E> {
  private final String label;
  private final Class<E> enumType;
  private JComboBox<E> comboBox;
  private final E defaultValue;

  public ComboBoxInputField(String label, Class<E> enumType, E defaultValue) {
    this.label = label;
    this.enumType = enumType;
    this.defaultValue = defaultValue;
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    comboBox = new JComboBox<E>(enumType.getEnumConstants());
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
