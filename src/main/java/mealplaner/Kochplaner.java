package mealplaner;

import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;

import javax.swing.JFrame;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.recipes.provider.IngredientProvider;

public final class Kochplaner {
  private Kochplaner() {
  }

  public static void main(String[] args) {
    IngredientProvider ingredientProvider = loadIngredientProvider("savefiles/ingredients.xml");
    invokeLater(createMainGui(ingredientProvider));
  }

  private static Runnable createMainGui(IngredientProvider ingredientProvider) {
    return () -> {
      JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
      FileIoGui fileIoGui = new FileIoGui(mainFrame, "savefiles/");
      MealplanerData data = fileIoGui.loadDatabase();
      DialogFactory dialogFactory = new DialogFactory(mainFrame);
      new MainGui(mainFrame, data, ingredientProvider, dialogFactory, fileIoGui);
    };
  }
}