package mealplaner.plugins.api;

import java.util.Optional;
import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;

public interface PluginDescription {
  void registerPlugins(PluginStore pluginStore);

  Optional<ResourceBundle> getMessageBundle();

  Optional<ResourceBundle> getErrorBundle();
}
