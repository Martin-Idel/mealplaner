// SPDX-License-Identifier: MIT

package mealplaner;

import static java.util.ServiceLoader.load;
import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.ServiceLoader;
import javax.swing.JFrame;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIo;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.builtins.courses.BuiltinCoursesPlugin;
import mealplaner.proposal.ProposalBuilderFactoryImpl;

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

      FileIo fileIo = new FileIo(mainFrame, SAVE_PATH, pluginStore);
      MealplanerData data = fileIo.loadDatabase(pluginStore);
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      new MainGui(mainFrame, data, dialogFactory, fileIo, new ProposalBuilderFactoryImpl(), pluginStore);
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
