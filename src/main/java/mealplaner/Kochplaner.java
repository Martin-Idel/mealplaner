// SPDX-License-Identifier: MIT

package mealplaner;

import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;

import javax.swing.JFrame;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.plugins.cookingtime.CookingTimePlugin;
import mealplaner.plugins.plugins.preference.CookingPreferencePlugin;

public final class Kochplaner {
  private static final String SAVE_PATH = "savefiles/";

  private Kochplaner() {
  }

  public static void main(String[] args) {
    invokeLater(createMainGui());
  }

  private static Runnable createMainGui() {
    return () -> {
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      PluginStore pluginStore = registerPlugins();

      FileIoGui fileIoGui = new FileIoGui(mainFrame, SAVE_PATH, pluginStore);
      MealplanerData data = fileIoGui.loadDatabase(pluginStore);
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      new MainGui(mainFrame, data, dialogFactory, fileIoGui, pluginStore);
    };
  }

  public static PluginStore registerPlugins() {
    PluginStore pluginStore = new PluginStore();
    var cookingTimePlugin = new CookingTimePlugin();
    cookingTimePlugin.registerPlugins(pluginStore);
    var cookingPreferencePlugin = new CookingPreferencePlugin();
    cookingPreferencePlugin.registerPlugins(pluginStore);
    return pluginStore;
  }
}