package mealplaner.recepies.gui.dialogs.recepies;

import java.awt.BorderLayout;
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
import mealplaner.recepies.model.Recipe;
import mealplaner.recepies.provider.IngredientProvider;

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
		display(recipe, bundles, ingredients);
		dispose();
		return enteredRecipe;
	}

	private void display(Optional<Recipe> recipe,
			BundleStore bundles, IngredientProvider ingredients) {
		nonnegativeIntegerInputField = new NonnegativeIntegerInputField(
				bundles.message("recipeNumberOfPortionsField"),
				new NonnegativeInteger(4));
		JScrollPane tablescroll = new JScrollPane(
				recipeTable.setupTable(recipe, bundles, ingredients));
		JPanel buttonPanel = displayButtons(bundles);
		nonnegativeIntegerInputField.addToPanel(dataPanel);
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
