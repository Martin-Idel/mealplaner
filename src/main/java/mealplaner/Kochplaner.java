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
      PluginStore pluginStore = new PluginStore();
      FileIoGui fileIoGui = new FileIoGui(mainFrame, SAVE_PATH, pluginStore);
      MealplanerData data = fileIoGui.loadDatabase();
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      new MainGui(mainFrame, data, dialogFactory, fileIoGui);
    };
  }
}