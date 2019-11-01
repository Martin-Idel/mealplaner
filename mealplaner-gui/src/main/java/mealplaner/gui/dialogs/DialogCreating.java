// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs;

import mealplaner.model.DataStore;
import mealplaner.plugins.PluginStore;

public interface DialogCreating<T> {
  T showDialog(DataStore data, PluginStore pluginStore);
}
