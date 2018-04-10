package mealplaner.gui.factories;

import static mealplaner.gui.dialogs.proposaloutput.ProposalTable.proposalOutput;

import javax.swing.JFrame;

import mealplaner.gui.dialogs.mealinput.MultipleMealInput;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.pastupdate.UpdatePastMeals;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.proposaloutput.ProposalTable;
import mealplaner.gui.dialogs.settingsinput.DefaultSettingsInput;
import mealplaner.gui.dialogs.settingsinput.ProposalSettingsInput;
import mealplaner.recipes.gui.dialogs.ingredients.IngredientsInput;
import mealplaner.shopping.ShoppingListDialog;

public class DialogFactory {
  private final JFrame frame;

  public DialogFactory(JFrame parentFrame) {
    this.frame = parentFrame;
  }

  public MultipleMealInput createMultipleMealInputDialog() {
    return new MultipleMealInput(frame);
  }

  public SingleMealInput createSingleMealInputDialog() {
    return new SingleMealInput(frame);
  }

  public UpdatePastMeals createUpdatePastMealDialog() {
    return new UpdatePastMeals(frame);
  }

  public ProposalOutput createProposalOutputDialog() {
    return new ProposalOutput(frame);
  }

  public DefaultSettingsInput createDefaultSettingsDialog() {
    return new DefaultSettingsInput(frame);
  }

  public ProposalSettingsInput createProposalSettingsDialog() {
    return new ProposalSettingsInput(frame);
  }

  public ProposalTable createProposalTableFactory() {
    return proposalOutput();
  }

  public IngredientsInput createIngredientsInput() {
    return new IngredientsInput(frame);
  }

  public ShoppingListDialog createShoppingListDialog() {
    return new ShoppingListDialog(frame);
  }
}
