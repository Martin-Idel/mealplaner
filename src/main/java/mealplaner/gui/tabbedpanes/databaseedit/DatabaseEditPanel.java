package mealplaner.gui.tabbedpanes.databaseedit;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.MealplanerData;
import mealplaner.commons.gui.JMenuBuilder;
import mealplaner.gui.MainContainer;
import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public class DatabaseEditPanel {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final IngredientProvider ingredients;
  private DatabaseEdit dbaseEdit;

  public DatabaseEditPanel(MealplanerData mealPlan,
      JFrame frame,
      IngredientProvider ingredients) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.ingredients = ingredients;
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("dataPanelName"), setupDatabasePanel());
    container.addToFileMenu(printDatabaseMenu(action -> dbaseEdit.printTable()));
    container.addSeparatorToFileMenu();
    container.addToHelpMenu(helpDatabaseMenu());
  }

  private JMenuItem helpDatabaseMenu() {
    return new JMenuBuilder().addLabelText(BUNDLES.message("menuHelpProposal"))
        .addMnemonic(BUNDLES.message("menuHelpProposalMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpProposalText"),
            BUNDLES.message("helpProposalTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build();
  }

  private JMenuItem printDatabaseMenu(ActionListener listener) {
    return new JMenuBuilder().addLabelText(BUNDLES.message("menuDataPrintDatabase"))
        .addMnemonic(BUNDLES.message("menuDataPrintDatabaseMnemonic"))
        .addActionListener(listener)
        .build();
  }

  private JPanel setupDatabasePanel() {
    JPanel databasePanel = new JPanel();
    dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel);
    dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals), ingredients);
    return databasePanel;
  }

  public void saveDatabase() {
    if (unsavedChanges()) {
      mealPlan.setMeals(dbaseEdit.getMeals());
    }
  }

  private boolean unsavedChanges() {
    List<Meal> meals = dbaseEdit.getMeals();
    List<Meal> savedMeals = mealPlan.getMeals();
    return !meals.stream()
        .allMatch(meal -> savedMeals.stream().anyMatch(savedMeal -> savedMeal.equals(meal)))
        || !savedMeals.stream()
            .allMatch(savedMeal -> meals.stream().anyMatch(meal -> meal.equals(savedMeal)));
  }
}
