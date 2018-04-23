package mealplaner.gui.dialogs.pastupdate;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.proposal.ProposedMenu;

public class UpdatePastMeals implements DialogCreating<Optional<List<ProposedMenu>>> {
  private final DialogWindow dialogWindow;
  private final UpdateTable updateTable;
  private Optional<List<ProposedMenu>> changedMeals = empty();

  public UpdatePastMeals(JFrame parentFrame) {
    dialogWindow = window(parentFrame, BUNDLES.message("updatePastMealsDialogTitle"),
        "UpdateDialog");
    updateTable = new UpdateTable();
  }

  @Override
  public Optional<List<ProposedMenu>> showDialog(DataStore mealPlan) {
    display(mealPlan);
    return changedMeals;
  }

  private void display(DataStore mealPlan) {
    updateTable.createTable(mealPlan.getLastProposal(),
        mealPlan, mealPlan.getDaysPassed());

    ButtonPanel buttonPanel = createButtonPanel();

    dialogWindow.addCentral(updateTable.getTable());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    if (mealPlan.getDaysPassed() != 0) {
      dialogWindow.setVisible();
    }
  }

  private ButtonPanel createButtonPanel() {
    return builder("UpdatePastMeals")
        .addSaveButton(action -> {
          changedMeals = of(updateTable.returnContent());
          dialogWindow.dispose();
        })
        .addCancelDialogButton(dialogWindow)
        .build();
  }
}