package mealplaner.commons.gui.inputfields;

import javax.swing.JPanel;

public interface InputField<T> {
  void addToPanel(JPanel panel);

  T getUserInput();

  void resetField();
}
