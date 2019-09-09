package mealplaner.plugins.api;

import mealplaner.commons.BundleStore;

public interface PluginDescription {
  void registerPlugins();

  void registerMessageBundles(BundleStore bundleStore);
}
