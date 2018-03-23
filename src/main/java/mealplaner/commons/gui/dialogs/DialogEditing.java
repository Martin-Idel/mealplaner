package mealplaner.commons.gui.dialogs;

import mealplaner.DataStore;

public interface DialogEditing<T> {
  T showDialog(T toEdit, DataStore data);
}
