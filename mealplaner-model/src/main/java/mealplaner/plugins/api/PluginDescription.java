// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;

public interface PluginDescription {
  void registerPlugins(PluginStore pluginStore);

  Optional<ResourceBundle> getMessageBundle(Locale locale);

  Optional<ResourceBundle> getErrorBundle(Locale locale);
}
