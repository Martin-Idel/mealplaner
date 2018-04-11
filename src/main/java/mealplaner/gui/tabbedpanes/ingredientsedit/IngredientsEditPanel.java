package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.gui.MainContainer;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;

public class IngredientsEditPanel {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final FileIoGui fileIoGui;
  private IngredientsEdit ingredientsEdit;

  public IngredientsEditPanel(MealplanerData mealPlan,
      JFrame frame, FileIoGui fileIoGui) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.fileIoGui = fileIoGui;
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("ingredientsPanelName"), setupIngredientsPanel());
  }

  private JPanel setupIngredientsPanel() {
    JPanel databasePanel = new JPanel();
    ingredientsEdit = new IngredientsEdit(this.mealPlan, frame, databasePanel, fileIoGui);
    ingredientsEdit.setupPane(ingredients -> mealPlan.setIngredients(ingredients));
    return databasePanel;
  }
}
