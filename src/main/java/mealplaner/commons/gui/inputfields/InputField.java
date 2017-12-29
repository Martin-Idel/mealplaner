package mealplaner.commons.gui.inputfields;

import mealplaner.commons.gui.GuiPanel;

public interface InputField<T> {
  void addToPanel(GuiPanel panel);

  T getUserInput();

  void resetField();
}
