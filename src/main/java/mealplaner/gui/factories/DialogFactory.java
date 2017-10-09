package mealplaner.gui.factories;

import javax.swing.JFrame;

import mealplaner.BundleStore;
import mealplaner.gui.dialogs.mealinput.MultipleMealInput;
import mealplaner.gui.dialogs.mealinput.SingleMealInput;
import mealplaner.gui.dialogs.pastupdate.UpdatePastMeals;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.proposaloutput.ProposalTableFactory;
import mealplaner.gui.dialogs.settingsinput.DefaultSettingsInput;
import mealplaner.gui.dialogs.settingsinput.ProposalSettingsInput;
import mealplaner.recepies.gui.dialogs.ingredients.IngredientsInput;
import mealplaner.recepies.provider.IngredientProvider;

public class DialogFactory {
	private JFrame frame;
	private BundleStore bundles;

	public DialogFactory(JFrame parentFrame, BundleStore bundles) {
		this.frame = parentFrame;
		this.bundles = bundles;
	}

	public MultipleMealInput createMultipleMealInputDialog(IngredientProvider ingredientProvider) {
		return new MultipleMealInput(frame, bundles, ingredientProvider);
	}

	public SingleMealInput createSingleMealInputDialog(IngredientProvider ingredientProvider) {
		return new SingleMealInput(frame, bundles, ingredientProvider);
	}

	public UpdatePastMeals createUpdatePastMealDialog() {
		return new UpdatePastMeals(frame, bundles);
	}

	public ProposalOutput createProposalOutputDialog() {
		return new ProposalOutput(frame, bundles);
	}

	public DefaultSettingsInput createDefaultSettingsDialog() {
		return new DefaultSettingsInput(frame, bundles);
	}

	public ProposalSettingsInput createProposalSettingsDialog() {
		return new ProposalSettingsInput(frame, bundles);
	}

	public ProposalTableFactory createProposalTableFactory() {
		return new ProposalTableFactory(bundles);
	}

	public IngredientsInput createIngredientsInput() {
		return new IngredientsInput(frame, bundles);
	}
}
