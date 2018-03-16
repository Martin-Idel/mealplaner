package mealplaner.gui.tabbedpanes.databaseedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.JMenuBuilder.builder;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import mealplaner.MealplanerData;
import mealplaner.gui.MainContainer;
import mealplaner.io.FileIoGui;
import mealplaner.model.Meal;

public class DatabaseEditPanel {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final FileIoGui fileIoGui;
  private DatabaseEdit dbaseEdit;

  public DatabaseEditPanel(MealplanerData mealPlan,
      JFrame frame, FileIoGui fileIoGui) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.fileIoGui = fileIoGui;
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("dataPanelName"), setupDatabasePanel());
    container.addToFileMenu(printDatabaseMenu(action -> dbaseEdit.printTable()));
    container.addSeparatorToFileMenu();
    container.addToHelpMenu(helpDatabaseMenu());
  }

  private JMenuItem helpDatabaseMenu() {
    return builder("HelpDataBase").addLabelText(BUNDLES.message("menuHelpDatabase"))
        .addMnemonic(BUNDLES.message("menuHelpDatabaseMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            BUNDLES.message("helpDatabaseText"),
            BUNDLES.message("helpDatabaseTitle"), JOptionPane.INFORMATION_MESSAGE))
        .build();
  }

  private JMenuItem printDatabaseMenu(ActionListener listener) {

    return builder("PrintDatabase").addLabelText(BUNDLES.message("menuDataPrintDatabase"))
        .addMnemonic(BUNDLES.message("menuDataPrintDatabaseMnemonic"))
        .addActionListener(listener)
        .build();
  }

  private JPanel setupDatabasePanel() {
    JPanel databasePanel = new JPanel();
    dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel, fileIoGui);
    dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals));
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
