package mealplaner.commons.gui.dialogs;

import mealplaner.DataStore;

public interface DialogCreating<T> {
  T showDialog(DataStore data);
}
