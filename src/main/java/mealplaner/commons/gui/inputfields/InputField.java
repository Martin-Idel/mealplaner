package mealplaner.commons.gui.inputfields;

import mealplaner.commons.gui.GuiPanel;

/**
 * An input field is a swing abstraction over a field inside a grid layout with
 * a label and an input component, where the user can enter some values.
 *
 * @param <T>
 *          Content type of the Input field
 */
public interface InputField<T> {
  /**
   * Add the input field to a GuiPanel - a low powered abstraction over Swing
   * panels.
   * 
   * @param panel
   */
  void addToPanel(GuiPanel panel);

  /**
   * Read the user input value from this field.
   * 
   * @return The value entered by the user.
   */
  T getUserInput();

  /**
   * Reset the field's content to some prespecified default.
   */
  void resetField();
}
