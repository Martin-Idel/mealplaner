package mealplaner.gui.dialogs.mealinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.dialogs.mealinput.MealInputGrid.inputGrid;

import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.MessageDialog;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public abstract class MealInput<T> {
  private final JFrame parentFrame;
  private final DialogWindow dialogWindow;

  private final MealInputGrid inputGrid;

  public MealInput(JFrame parent) {
    dialogWindow = DialogWindow.window(parent, BUNDLES.message("mealInputDialogTitle"));
    this.parentFrame = parent;
    inputGrid = inputGrid(dialogWindow);
  }

  public abstract T showDialog(IngredientProvider ingredientProvider);

  protected void display(IngredientProvider ingredientProvider, ActionListener saveListener) {
    GridPanel mealCreationPanel = inputGrid.initialiseInputFields(ingredientProvider);

    ButtonPanel buttonPanel = buildButtonPanel(saveListener);

    dialogWindow.addCentral(mealCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 400);
    dialogWindow.setVisible();
  }

  private ButtonPanel buildButtonPanel(ActionListener saveListener) {
    return builder("MealInput")
        .addSaveButton(saveListener)
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  protected void resetFields() {
    inputGrid.resetFields();
  }

  protected void dispose() {
    dialogWindow.dispose();
  }

  protected Optional<Meal> getMealAndShowDialog() {
    Optional<Meal> mealFromInput = inputGrid.getMealFromUserInput();
    if (!mealFromInput.isPresent()) {
      MessageDialog.userErrorMessage(parentFrame, BUNDLES.message("menuNameChoiceEmpty"));
    }
    return mealFromInput;
  }
}