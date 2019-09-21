// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs;

import mealplaner.model.DataStore;
import mealplaner.plugins.PluginStore;

public interface DialogCreatingWithAdditional<T1, T2> {
  T2 showDialog(T1 additionalInformation, DataStore data, PluginStore pluginStore);
}
