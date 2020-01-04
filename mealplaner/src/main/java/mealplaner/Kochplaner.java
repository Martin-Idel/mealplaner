// SPDX-License-Identifier: MIT

package mealplaner;

import static java.util.ServiceLoader.load;
import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.ServiceLoader;

import javax.swing.JFrame;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.builtins.courses.BuiltinCoursesPlugin;

public final class Kochplaner {
  private static final String SAVE_PATH = "savefiles/";

  private Kochplaner() {
  }

  public static void main(String[] args) {
    invokeLater(createMainGui());
  }

  // TODO: Create savegame folder if not already present
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
    var coursesBuiltin = new BuiltinCoursesPlugin();
    coursesBuiltin.registerPlugins(pluginStore);
    ServiceLoader<PluginDescription> loader = load(PluginDescription.class);
    for (var plugin : loader) {
      plugin.registerPlugins(pluginStore);
    }
    return pluginStore;
  }
}