package mealplaner.gui.dialogs.pastupdate;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.model.Meal;

public class UpdatePastMeals {
  private final DialogWindow dialogWindow;
  private final UpdateTable updateTable;
  private Optional<List<Meal>> changedMeals = empty();

  public UpdatePastMeals(JFrame parentFrame) {
    dialogWindow = window(parentFrame, BUNDLES.message("updatePastMealsDialogTitle"));
    updateTable = new UpdateTable();
  }

  public Optional<List<Meal>> showDialog(DataStore mealPlan) {
    display(mealPlan);
    return changedMeals;
  }

  private void display(DataStore mealPlan) {
    updateTable.createTable(mealPlan.getLastProposal(),
        mealPlan.getMeals(), mealPlan.getDaysPassed());

    JPanel buttonPanel = displayButtons();

    dialogWindow.addCentral(updateTable.getTable().getTableInScrollPane());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    if (mealPlan.getDaysPassed() != 0) {
      dialogWindow.setVisible();
    }
  }

  private JPanel displayButtons() {
    return new ButtonPanelBuilder()
        .addSaveButton(action -> {
          changedMeals = of(updateTable.returnContent());
          dialogWindow.dispose();
        })
        .addCancelDialogButton(dialogWindow)
        .build();
  }
}