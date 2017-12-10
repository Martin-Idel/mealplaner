package mealplaner.commons.gui.inputfields;

import javax.swing.JPanel;

public interface InputField<T> {
  public void addToPanel(JPanel panel);

  public T getUserInput();

  public void resetField();
}
