// SPDX-License-Identifier: MIT

package mealplaner.plugins.seasonal.gui;

import java.util.function.Consumer;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import mealplaner.commons.gui.GuiPanel;
import mealplaner.plugins.seasonal.ingredientextension.Seasonality;

public class SeasonalityComboBoxWithHook {
  private JComboBox<Seasonality> comboBox;
  private final String label;
  private final String name;
  private final Seasonality defaultValue;
  private final int orderNumber;
  private Consumer<Void> onChangeHook;

  public SeasonalityComboBoxWithHook(
      String label,
      String name,
      Seasonality defaultValue,
      int orderNumber) {
    this.label = label;
    this.name = name;
    this.defaultValue = defaultValue;
    this.orderNumber = orderNumber;
  }

  public void setOnChange(Consumer<Void> onChangeHook) {
    this.onChangeHook = onChangeHook;
  }

  public void addToPanel(GuiPanel panel) {
    comboBox = new JComboBox<>(Seasonality.values());
    comboBox.setName("InputFieldComboBox" + name);
    comboBox.addActionListener(e -> {
      if (onChangeHook != null) {
        onChangeHook.accept(null);
      }
    });
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(comboBox);
  }

  public Seasonality getUserInput() {
    return comboBox.getItemAt(comboBox.getSelectedIndex());
  }

  public void resetField() {
    comboBox.setSelectedItem(defaultValue);
  }

  public int getOrdering() {
    return orderNumber;
  }
}
