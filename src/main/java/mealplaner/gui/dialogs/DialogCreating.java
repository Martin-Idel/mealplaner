package mealplaner.gui.dialogs;

import mealplaner.model.DataStore;

public interface DialogCreating<T> {
  T showDialog(DataStore data);
}
