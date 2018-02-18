package mealplaner;

import static javax.swing.SwingUtilities.invokeLater;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;

import javax.swing.JFrame;

import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.recipes.provider.IngredientProvider;

public final class Kochplaner {
  private Kochplaner() {
  }

  public static void main(String[] args) {
    MealplanerData data = new MealplanerData();
    IngredientProvider ingredientProvider = loadIngredientProvider("ingredients.xml");
    invokeLater(createMainGui(data, ingredientProvider));
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