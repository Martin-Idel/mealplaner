package mealplaner.gui.dialogs.mealinput;

import static java.util.Optional.of;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.recipes.model.Recipe.createRecipe;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.commons.ButtonInputField;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.ComboBoxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonEmptyTextInputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.gui.commons.TextField;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.gui.dialogs.recepies.RecipeInput;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public abstract class MealInput extends JDialog {
	private static final long serialVersionUID = 1L;
	private JFrame parentFrame;
	private JPanel mealCreationPanel;
	private JPanel buttonPanel;

	private InputField<Optional<String>> nameField;
	private InputField<CookingTime> cookingTimeField;
	private InputField<Sidedish> sidedishField;
	private InputField<ObligatoryUtensil> obligatoryUtensilField;
	private InputField<NonnegativeInteger> daysPassedField;
	private InputField<CookingPreference> preferenceField;
	private InputField<String> commentField;
	private InputField<Optional<Recipe>> recipeInputField;

	public MealInput(JFrame parent, IngredientProvider ingredientProvider) {
		super(parent, BUNDLES.message("mealInputDialogTitle"), true);
		this.parentFrame = parent;

		nameField = new NonEmptyTextInputField(BUNDLES.message("insertMealName"));
		cookingTimeField = new ComboBoxInputField<CookingTime>(
				BUNDLES.message("insertMealLength"),
				CookingTime.class,
				CookingTime.SHORT);
		sidedishField = new ComboBoxInputField<Sidedish>(
				BUNDLES.message("insertMealSidedish"),
				Sidedish.class,
				Sidedish.NONE);
		obligatoryUtensilField = new ComboBoxInputField<ObligatoryUtensil>(
				BUNDLES.message("insertMealUtensil"),
				ObligatoryUtensil.class,
				ObligatoryUtensil.POT);
		daysPassedField = new NonnegativeIntegerInputField(
				BUNDLES.message("insertMealLastCooked"),
				new NonnegativeInteger(0));
		preferenceField = new ComboBoxInputField<CookingPreference>(
				BUNDLES.message("insertMealPopularity"),
				CookingPreference.class,
				CookingPreference.NO_PREFERENCE);
		commentField = new TextField(BUNDLES.message("insertMealComment"));
		recipeInputField = new ButtonInputField<Optional<Recipe>>(
				BUNDLES.message("createRecipeLabel"),
				BUNDLES.message("editRecipeButtonLabel"),
				BUNDLES.message("createRecipeButtonLabel"),
				of(createRecipe()), content -> {
					return createRecipeDialog(ingredientProvider, content);
				});
	}

	private Optional<Recipe> createRecipeDialog(IngredientProvider ingredientProvider,
			Optional<Recipe> recipe) {
		RecipeInput recipeInput = new RecipeInput(parentFrame,
				BUNDLES.message("recipeInputDialogTitle"));
		return recipeInput.showDialog(recipe, ingredientProvider);
	}

	protected void display(ActionListener saveListener) {
		mealCreationPanel = new JPanel();
		mealCreationPanel.setLayout(new GridLayout(0, 2));

		allFields().forEach(field -> field.addToPanel(mealCreationPanel));

		buttonPanel = new ButtonPanelBuilder()
				.addSaveButton(saveListener)
				.addCancelDialogButton(this)
				.build();

		setupPanelsInDialogFrame();
		setVisible(true);
	}

	private void setupPanelsInDialogFrame() {
		JPanel dialogPanel = new JPanel();
		dialogPanel.setLayout(new BorderLayout());
		dialogPanel.add(mealCreationPanel, BorderLayout.CENTER);
		dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

		getContentPane().add(dialogPanel);
		pack();
		setLocationRelativeTo(parentFrame);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	protected void resetFields() {
		allFields().forEach(InputField::resetField);
	}

	protected Optional<Meal> getMealAndShowDialog() {
		Optional<Meal> mealFromInput = getMealFromUserInput();
		if (!mealFromInput.isPresent()) {
			JOptionPane.showMessageDialog(null, BUNDLES.message("menuNameChoiceEmpty"),
					BUNDLES.message("errorHeading"), JOptionPane.INFORMATION_MESSAGE);
		}
		return mealFromInput;
	}

	private Stream<InputField<?>> allFields() {
		return Arrays.asList(nameField, cookingTimeField, sidedishField, obligatoryUtensilField,
				daysPassedField, preferenceField, commentField, recipeInputField).stream();
	}

	private Optional<Meal> getMealFromUserInput() {
		return nameField.getUserInput().isPresent()
				? of(createMeal(nameField.getUserInput().get(),
						cookingTimeField.getUserInput(),
						sidedishField.getUserInput(),
						obligatoryUtensilField.getUserInput(),
						preferenceField.getUserInput(),
						daysPassedField.getUserInput().value,
						commentField.getUserInput(),
						recipeInputField.getUserInput()))
				: Optional.empty();
	}
}