// SPDX-License-Identifier: MIT

package testcommons;

import static mealplaner.commons.BundleStore.BUNDLES;

import mealplaner.plugins.api.PluginDescription;

public final class PluginsUtils {
  private PluginsUtils() {
  }

  public static void setupMessageBundles(PluginDescription plugin) {
    plugin.getMessageBundle(BUNDLES.locale()).ifPresent(BUNDLES::addMessageBundle);
    plugin.getErrorBundle(BUNDLES.locale()).ifPresent(BUNDLES::addErrorBundle);
  }
}
