package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.HelpPanel.mealPlanerHelpScrollPane;
import static mealplaner.commons.gui.JMenuBuilder.builder;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    container.addToHelpMenu(helpIngredientsMenu());
  }

  private JPanel setupIngredientsPanel() {
    JPanel databasePanel = new JPanel();
    ingredientsEdit = new IngredientsEdit(this.mealPlan, frame, databasePanel,
        fileIoGui);
    ingredientsEdit.setupPane();
    return databasePanel;
  }

  public void saveDatabase() {
    ingredientsEdit.saveIngredients();
  }

  private JMenuItem helpIngredientsMenu() {
    return builder("HelpDataBase").addLabelText(BUNDLES.message("menuHelpIngredients"))
        .addMnemonic(BUNDLES.message("menuHelpIngredientsMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            mealPlanerHelpScrollPane("IngredientsEditHelp"),
            BUNDLES.message("menuHelp"), JOptionPane.PLAIN_MESSAGE))
        .build();
  }

}
