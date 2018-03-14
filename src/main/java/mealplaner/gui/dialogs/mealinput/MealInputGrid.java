package mealplaner.gui.dialogs.mealinput;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.model.Meal.createMeal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
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
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Recipe;

public final class MealInputGrid {
  private InputField<Optional<String>> nameField;
  private InputField<CookingTime> cookingTimeField;
  private InputField<Sidedish> sidedishField;
  private InputField<ObligatoryUtensil> obligatoryUtensilField;
  private InputField<NonnegativeInteger> daysPassedField;
  private InputField<CookingPreference> preferenceField;
  private InputField<String> commentField;
  private InputField<Optional<Recipe>> recipeInputField;

  private final DialogWindow dialogWindow;

  private MealInputGrid(DialogWindow dialogWindow) {
    this.dialogWindow = dialogWindow;
  }

  public static MealInputGrid inputGrid(DialogWindow dialog) {
    return new MealInputGrid(dialog);
  }

  public GridPanel initialiseInputFields(List<Ingredient> ingredients) {
    nameField = new NonEmptyTextInputField(BUNDLES.message("insertMealName"), "Name");
    cookingTimeField = new ComboBoxInputField<CookingTime>(
        BUNDLES.message("insertMealLength"),
        "CookingTime",
        CookingTime.class,
        CookingTime.SHORT);
    sidedishField = new ComboBoxInputField<Sidedish>(
        BUNDLES.message("insertMealSidedish"),
        "Sidedish",
        Sidedish.class,
        Sidedish.NONE);
    obligatoryUtensilField = new ComboBoxInputField<ObligatoryUtensil>(
        BUNDLES.message("insertMealUtensil"),
        "ObligatoryUtensil",
        ObligatoryUtensil.class,
        ObligatoryUtensil.POT);
    daysPassedField = new NonnegativeIntegerInputField(
        BUNDLES.message("insertMealLastCooked"),
        "DaysPassed",
        ZERO);
    preferenceField = new ComboBoxInputField<CookingPreference>(
        BUNDLES.message("insertMealPopularity"),
        "CookingPreference",
        CookingPreference.class,
        CookingPreference.NO_PREFERENCE);
    commentField = new TextInputField(BUNDLES.message("insertMealComment"), "Comment");
    recipeInputField = new ButtonInputField<Optional<Recipe>>(
        BUNDLES.message("createRecipeLabel"),
        "Recipe",
        BUNDLES.message("editRecipeButtonLabel"),
        BUNDLES.message("createRecipeButtonLabel"),
        empty(), content -> createRecipeDialog(ingredients, content));

    GridPanel mealCreationPanel = gridPanel(0, 2);
    allFields().forEach(field -> field.addToPanel(mealCreationPanel));
    return mealCreationPanel;
  }

  private Optional<Recipe> createRecipeDialog(List<Ingredient> ingredients,
      Optional<Recipe> recipe) {
    RecipeInput recipeInput = new RecipeInput(dialogWindow,
        BUNDLES.message("recipeInputDialogTitle"));
    return recipeInput.showDialog(recipe, ingredients);
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
