package mealplaner;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;

import java.util.MissingResourceException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.recipes.provider.IngredientProvider;

public class Kochplaner {
  private static final Logger logger = LoggerFactory.getLogger(Kochplaner.class);

  public static void main(String[] args) {
    try {
      MealplanerData data = new MealplanerData();
      IngredientProvider ingredientProvider = loadIngredientProvider();
      invokeLater(createMainGui(data, ingredientProvider));
    } catch (MissingResourceException exc) {
      logger.error(
          "Fatal error: Resource bundles could not be found. No localisation possible.",
          exc);
      JOptionPane.showMessageDialog(null, "Fatal error: Resource Bundles not found", "Error",
          ERROR_MESSAGE);
    }
  }

  private static Runnable createMainGui(MealplanerData data,
      IngredientProvider ingredientProvider) {
    return () -> {
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      new MainGui(mainFrame, data, dialogFactory, ingredientProvider);
    };
  }
}