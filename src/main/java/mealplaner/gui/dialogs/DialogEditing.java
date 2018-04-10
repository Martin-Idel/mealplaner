package mealplaner.gui.dialogs;

import mealplaner.model.DataStore;

public interface DialogEditing<T> {
  T showDialog(T toEdit, DataStore data);
}
