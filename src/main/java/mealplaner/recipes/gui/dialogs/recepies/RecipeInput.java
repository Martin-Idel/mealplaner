package mealplaner.recipes.gui.dialogs.recepies;

import static mealplaner.commons.NonnegativeInteger.nonNegative;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import mealplaner.BundleStore;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public class RecipeInput extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel dataPanel;
	private RecipeTable recipeTable;
	private InputField<NonnegativeInteger> nonnegativeIntegerInputField;
	private Optional<Recipe> enteredRecipe;

	public RecipeInput(JFrame parentFrame, String label) {
		super(parentFrame, label, true);
		this.parentFrame = parentFrame;
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new BorderLayout());
		recipeTable = new RecipeTable();
	}

	public Optional<Recipe> showDialog(Optional<Recipe> recipe,
			BundleStore bundles, IngredientProvider ingredients) {
		enteredRecipe = recipe;
		display(recipe, bundles, ingredients);
		dispose();
		return enteredRecipe;
	}

	private void display(Optional<Recipe> recipe,
			BundleStore bundles, IngredientProvider ingredients) {
		nonnegativeIntegerInputField = new NonnegativeIntegerInputField(
				bundles.message("recipeNumberOfPortionsField"),
				recipe.isPresent()
						? nonNegative(recipe.get().getNumberOfPortions())
						: nonNegative(4));
		JPanel inputFieldPanel = new JPanel();
		inputFieldPanel.setLayout(new GridLayout(0, 2));
		nonnegativeIntegerInputField.addToPanel(inputFieldPanel);
		JScrollPane tablescroll = new JScrollPane(
				recipeTable.setupTable(recipe, bundles, ingredients));
		JPanel buttonPanel = displayButtons(bundles);
		dataPanel.add(inputFieldPanel, BorderLayout.NORTH);
		dataPanel.add(tablescroll, BorderLayout.CENTER);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);

		setSize(300, 300);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	private JPanel displayButtons(BundleStore bundles) {
		return new ButtonPanelBuilder(bundles)
				.addCancelDialogButton(this)
				.addOkButton(getSaveListener(recipeTable))
				.build();
	}

	private ActionListener getSaveListener(RecipeTable recipeTable) {
		return action -> {
			enteredRecipe = recipeTable
					.getRecipe(nonnegativeIntegerInputField.getUserInput().value);
			setVisible(false);
			dispose();
		};
	}
}
