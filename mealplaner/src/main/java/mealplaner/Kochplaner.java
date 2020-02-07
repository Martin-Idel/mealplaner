// SPDX-License-Identifier: MIT

package mealplaner;

import static java.util.ServiceLoader.load;
import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.io.File;
import java.nio.file.Path;
import java.util.ServiceLoader;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIo;
import mealplaner.model.MealplanerData;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.PluginDescription;
import mealplaner.plugins.builtins.courses.BuiltinCoursesPlugin;
import mealplaner.proposal.ProposalBuilderFactoryImpl;

public final class Kochplaner {
  private static Logger logger = LoggerFactory.getLogger(Kochplaner.class);
  private static final String SAVE_PATH = "savefiles" + File.separator;

  private Kochplaner() {
  }

  public static void main(String[] args) {
    invokeLater(createMainGui());
  }

  private static Runnable createMainGui() {
    return () -> {
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      PluginStore pluginStore = registerPlugins();
      if (!Path.of(SAVE_PATH).toFile().exists()) {
        var createDirectory = Path.of(SAVE_PATH).toFile().mkdirs();
        if (!createDirectory) {
          logger.error("Could not create directory for savefiles. Probably this is a permissions problem");
        }
      }

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
      plugin.getMessageBundle(BUNDLES.locale()).ifPresent(BUNDLES::addMessageBundle);
      plugin.getErrorBundle(BUNDLES.locale()).ifPresent(BUNDLES::addErrorBundle);
      plugin.registerPlugins(pluginStore);
    }
    return pluginStore;
  }
}
