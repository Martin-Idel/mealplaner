package mealplaner.gui.factories;

import javax.swing.JFrame;

import mealplaner.gui.dialogs.mealinput.MultipleMealInput;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.pastupdate.UpdatePastMeals;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.proposaloutput.ProposalTableFactory;
import mealplaner.gui.dialogs.settingsinput.DefaultSettingsInput;
import mealplaner.gui.dialogs.settingsinput.ProposalSettingsInput;
import mealplaner.recipes.gui.dialogs.ingredients.IngredientsInput;
import mealplaner.recipes.provider.IngredientProvider;
import mealplaner.shopping.ShoppingListDialog;

public class DialogFactory {
	private JFrame frame;

	public DialogFactory(JFrame parentFrame) {
		this.frame = parentFrame;
	}

	public MultipleMealInput createMultipleMealInputDialog(IngredientProvider ingredientProvider) {
		return new MultipleMealInput(frame, ingredientProvider);
	}

	public SingleMealInput createSingleMealInputDialog(IngredientProvider ingredientProvider) {
		return new SingleMealInput(frame, ingredientProvider);
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

	public ProposalTableFactory createProposalTableFactory() {
		return new ProposalTableFactory();
	}

	public IngredientsInput createIngredientsInput() {
		return new IngredientsInput(frame);
	}

	public ShoppingListDialog createShoppingListDialog() {
		return new ShoppingListDialog(frame);
	}
}
