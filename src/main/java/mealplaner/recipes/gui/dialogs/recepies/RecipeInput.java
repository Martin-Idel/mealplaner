package mealplaner.recipes.gui.dialogs.recepies;

import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Recipe.createRecipe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.gui.tables.Table;
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
	}

	public Optional<Recipe> showDialog(Optional<Recipe> recipe, IngredientProvider ingredients) {
		enteredRecipe = recipe;
		display(recipe, ingredients);
		dispose();
		return enteredRecipe;
	}

	private void display(Optional<Recipe> recipe, IngredientProvider ingredients) {
		nonnegativeIntegerInputField = new NonnegativeIntegerInputField(
				BUNDLES.message("recipeNumberOfPortionsField"),
				recipe.isPresent()
						? recipe.get().getNumberOfPortions()
						: nonNegative(4));
		JPanel inputFieldPanel = new JPanel();
		inputFieldPanel.setLayout(new GridLayout(0, 2));
		nonnegativeIntegerInputField.addToPanel(inputFieldPanel);
		recipeTable = new RecipeTable(recipe.orElse(createRecipe()), ingredients);
		Table table = recipeTable.setupTable();
		JPanel buttonPanel = displayButtons();
		dataPanel.add(inputFieldPanel, BorderLayout.NORTH);
		table.addScrollingTableToPane(dataPanel);
		dataPanel.add(buttonPanel, BorderLayout.SOUTH);
		getContentPane().add(dataPanel);

		setSize(300, 300);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
	}

	private JPanel displayButtons() {
		return new ButtonPanelBuilder()
				.addCancelDialogButton(this)
				.addOkButton(getSaveListener(recipeTable))
				.build();
	}

	private ActionListener getSaveListener(RecipeTable recipeTable) {
		return action -> {
			enteredRecipe = recipeTable.getRecipe(nonnegativeIntegerInputField.getUserInput());
			setVisible(false);
			dispose();
		};
	}
}
