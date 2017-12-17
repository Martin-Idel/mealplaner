package mealplaner.gui.dialogs.mealinput;

import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.recipes.model.Recipe.createRecipe;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JFrame;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.inputfields.ButtonInputField;
import mealplaner.commons.gui.inputfields.ComboBoxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonEmptyTextInputField;
import mealplaner.commons.gui.inputfields.NonnegativeIntegerInputField;
import mealplaner.commons.gui.inputfields.TextInputField;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.gui.dialogs.recepies.RecipeInput;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public final class MealInputGrid {
  private InputField<Optional<String>> nameField;
  private InputField<CookingTime> cookingTimeField;
  private InputField<Sidedish> sidedishField;
  private InputField<ObligatoryUtensil> obligatoryUtensilField;
  private InputField<NonnegativeInteger> daysPassedField;
  private InputField<CookingPreference> preferenceField;
  private InputField<String> commentField;
  private InputField<Optional<Recipe>> recipeInputField;

  private JFrame parentFrame;

  private MealInputGrid(JFrame parentFrame) {
    this.parentFrame = parentFrame;
  }

  public static MealInputGrid inputGrid(JFrame parentFrame) {
    return new MealInputGrid(parentFrame);
  }

  public GridPanel initialiseInputFields(IngredientProvider ingredientProvider) {
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
        ZERO);
    preferenceField = new ComboBoxInputField<CookingPreference>(
        BUNDLES.message("insertMealPopularity"),
        CookingPreference.class,
        CookingPreference.NO_PREFERENCE);
    commentField = new TextInputField(BUNDLES.message("insertMealComment"));
    recipeInputField = new ButtonInputField<Optional<Recipe>>(
        BUNDLES.message("createRecipeLabel"),
        BUNDLES.message("editRecipeButtonLabel"),
        BUNDLES.message("createRecipeButtonLabel"),
        of(createRecipe()), content -> createRecipeDialog(ingredientProvider, content));

    GridPanel mealCreationPanel = gridPanel(0, 2);
    allFields().forEach(field -> field.addToPanel(mealCreationPanel.getComponent()));
    return mealCreationPanel;
  }

  private Optional<Recipe> createRecipeDialog(IngredientProvider ingredientProvider,
      Optional<Recipe> recipe) {
    RecipeInput recipeInput = new RecipeInput(parentFrame,
        BUNDLES.message("recipeInputDialogTitle"));
    return recipeInput.showDialog(recipe, ingredientProvider);
  }

  public void resetFields() {
    allFields().forEach(InputField::resetField);
  }

  private Stream<InputField<?>> allFields() {
    return Arrays.asList(nameField, cookingTimeField, sidedishField, obligatoryUtensilField,
        daysPassedField, preferenceField, commentField, recipeInputField).stream();
  }

  public Optional<Meal> getMealFromUserInput() {
    return nameField.getUserInput().isPresent()
        ? of(createMeal(nameField.getUserInput().get(),
            cookingTimeField.getUserInput(),
            sidedishField.getUserInput(),
            obligatoryUtensilField.getUserInput(),
            preferenceField.getUserInput(),
            daysPassedField.getUserInput(),
            commentField.getUserInput(),
            recipeInputField.getUserInput()))
        : Optional.empty();
  }
}
