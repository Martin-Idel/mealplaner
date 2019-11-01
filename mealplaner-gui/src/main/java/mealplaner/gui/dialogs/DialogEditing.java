// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs;

import mealplaner.model.DataStore;
import mealplaner.plugins.PluginStore;

public interface DialogEditing<T> {
  T showDialog(T toEdit, DataStore data, PluginStore pluginStore);
}
