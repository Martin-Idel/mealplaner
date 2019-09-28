package mealplaner.plugins.api;

import java.util.ResourceBundle;

import mealplaner.plugins.PluginStore;

public interface PluginDescription {
  void registerPlugins(PluginStore pluginStore);

  ResourceBundle getMessageBundle();

  ResourceBundle getErrorBundle();
}
