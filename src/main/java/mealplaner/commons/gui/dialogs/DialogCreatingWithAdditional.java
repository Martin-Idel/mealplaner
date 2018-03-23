package mealplaner.commons.gui.dialogs;

import mealplaner.DataStore;

public interface DialogCreatingWithAdditional<T1, T2> {
  T2 showDialog(T1 additionalInformation, DataStore data);
}
