// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.mealinput;

import static java.util.Comparator.comparingInt;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.model.meal.Meal.createMeal;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
import mealplaner.gui.dialogs.recepies.RecipeInput;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.PluginStore;

public final class MealInputGrid {
  private InputField<Optional<String>> nameField;
  private InputField<CookingTime> cookingTimeField;
  private InputField<Sidedish> sidedishField;
  private InputField<ObligatoryUtensil> obligatoryUtensilField;
  private InputField<CourseType> courseTypeField;
  private InputField<NonnegativeInteger> daysPassedField;
  private InputField<CookingPreference> preferenceField;
  private InputField<String> commentField;
  private InputField<Optional<Recipe>> recipeInputField;
  private List<InputField<?>> mealFactFields;

  private final DialogWindow dialogWindow;

  private MealInputGrid(DialogWindow dialogWindow) {
    this.dialogWindow = dialogWindow;
  }

  public static MealInputGrid inputGrid(DialogWindow dialog) {
    return new MealInputGrid(dialog);
  }

  public GridPanel initialiseInputFields(DataStore mealPlan, PluginStore pluginStore) {
    nameField = new NonEmptyTextInputField(BUNDLES.message("insertMealName"), "Name", 0);
    cookingTimeField = new ComboBoxInputField<>(
            BUNDLES.message("insertMealLength"),
            "CookingTime",
            CookingTime.class,
            CookingTime.SHORT, 10);
    sidedishField = new ComboBoxInputField<>(
            BUNDLES.message("insertMealSidedish"),
            "Sidedish",
            Sidedish.class,
            Sidedish.NONE, 20);
    obligatoryUtensilField = new ComboBoxInputField<>(
            BUNDLES.message("insertMealUtensil"),
            "ObligatoryUtensil",
            ObligatoryUtensil.class,
            ObligatoryUtensil.POT, 30);
    daysPassedField = new NonnegativeIntegerInputField(
        BUNDLES.message("insertMealLastCooked"),
        "DaysPassed",
        ZERO, 40);
    preferenceField = new ComboBoxInputField<>(
            BUNDLES.message("insertMealPopularity"),
            "CookingPreference",
            CookingPreference.class,
            CookingPreference.NO_PREFERENCE, 50);
    courseTypeField = new ComboBoxInputField<>(
            BUNDLES.message("insertMealCourseType"),
            "CourseType",
            CourseType.class,
            CourseType.MAIN, 60);
    commentField = new TextInputField(BUNDLES.message("insertMealComment"), "Comment", 70);
    recipeInputField = new ButtonInputField<>(
            BUNDLES.message("createRecipeLabel"),
            "Recipe",
            BUNDLES.message("editRecipeButtonLabel"),
            BUNDLES.message("createRecipeButtonLabel"),
            empty(), content -> createRecipeDialog(mealPlan, content, pluginStore), 10000);
    mealFactFields = pluginStore
        .getRegisteredMealInputGuiExtensions()
        .stream()
        .flatMap(inputExtension -> inputExtension.createInputElements().stream())
        .collect(Collectors.toUnmodifiableList());

    GridPanel mealCreationPanel = gridPanel(0, 2);
    allFields().forEach(field -> field.addToPanel(mealCreationPanel));
    return mealCreationPanel;
  }

  private Optional<Recipe> createRecipeDialog(
      DataStore mealPlan, Optional<Recipe> recipe, PluginStore pluginStore) {
    RecipeInput recipeInput = new RecipeInput(dialogWindow);
    return recipeInput.showDialog(recipe, mealPlan, pluginStore);
  }

  public void resetFields() {
    allFields().forEach(InputField::resetField);
  }

  private Stream<InputField<?>> allFields() {
    return Stream.concat(
        Stream.of(nameField, cookingTimeField, sidedishField, obligatoryUtensilField,
        daysPassedField, preferenceField, courseTypeField, commentField, recipeInputField),
        mealFactFields.stream())
        .sorted(comparingInt(InputField::getOrdering));
  }

  public Optional<Meal> getMealFromUserInput() {
    return nameField.getUserInput().isPresent()
        ? of(createMeal(randomUUID(),
            nameField.getUserInput().get(),
            cookingTimeField.getUserInput(),
            sidedishField.getUserInput(),
            obligatoryUtensilField.getUserInput(),
            preferenceField.getUserInput(),
            courseTypeField.getUserInput(),
            daysPassedField.getUserInput(),
            commentField.getUserInput(),
            recipeInputField.getUserInput()))
        : Optional.empty();
  }
}
