package mealplaner.commons.gui.inputfields;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import mealplaner.commons.gui.GuiPanel;

public final class ComboBoxTextInputField implements InputField<String> {
  private final String label;
  private final String name;
  private JComboBox<String> autoCompleteBox;
  private final String defaultValue;
  private final List<String> alternatives;

  private ComboBoxTextInputField(String label, String name, String defaultValue,
      List<String> alternatives) {
    this.label = label;
    this.name = name;
    this.defaultValue = defaultValue;
    this.alternatives = alternatives;
  }

  public static ComboBoxTextInputField inputField(String label, String name, String defaultValue,
      List<String> alternatives) {
    return new ComboBoxTextInputField(label, name, defaultValue, alternatives);
  }

  @Override
  public void addToPanel(GuiPanel panel) {
    String[] ingredientsAlternatives = new String[alternatives.size()];
    autoCompleteBox = new JComboBox<String>(alternatives
        .toArray(ingredientsAlternatives));
    autoCompleteBox.setName("InputFieldComboBoxText" + name);
    AutoCompleteDecorator.decorate(autoCompleteBox);
    panel.getComponent().add(new JLabel(label));
    panel.getComponent().add(autoCompleteBox);
  }

  @Override
  public String getUserInput() {
    return (String) autoCompleteBox.getSelectedItem();
  }

  @Override
  public void resetField() {
    autoCompleteBox.setSelectedItem(defaultValue);
  }
}
