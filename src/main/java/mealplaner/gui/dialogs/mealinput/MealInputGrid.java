// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.mealinput;

import static java.util.Comparator.comparingInt;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.ZERO;
import static mealplaner.commons.gui.GridPanel.gridPanel;

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
import mealplaner.gui.dialogs.recepies.RecipeInput;
import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;

public final class MealInputGrid {
  private InputField<Optional<String>> nameField;
  private InputField<CourseType> courseTypeField;
  private InputField<NonnegativeInteger> daysPassedField;
  private InputField<Optional<Recipe>> recipeInputField;
  private List<InputField<MealFact>> mealFactFields;

  private final DialogWindow dialogWindow;

  private MealInputGrid(DialogWindow dialogWindow) {
    this.dialogWindow = dialogWindow;
  }

  public static MealInputGrid inputGrid(DialogWindow dialog) {
    return new MealInputGrid(dialog);
  }

  public GridPanel initialiseInputFields(DataStore mealPlan, PluginStore pluginStore) {
    nameField = new NonEmptyTextInputField(BUNDLES.message("insertMealName"), "Name", 0);
    daysPassedField = new NonnegativeIntegerInputField(
        BUNDLES.message("insertMealLastCooked"),
        "DaysPassed",
        ZERO, 40);
    courseTypeField = new ComboBoxInputField<>(
        BUNDLES.message("insertMealCourseType"),
        "CourseType",
        CourseType.class,
        CourseType.MAIN, 60);
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
        Stream.of(nameField, daysPassedField, courseTypeField, recipeInputField),
        mealFactFields.stream())
        .sorted(comparingInt(InputField::getOrdering));
  }

  public Optional<Meal> getMealFromUserInput(PluginStore pluginStore) {
    if (nameField.getUserInput().isEmpty()) {
      return Optional.empty();
    }

    var builder = MealBuilder.mealWithValidator(pluginStore)
        .name(nameField.getUserInput().get())
        .courseType(courseTypeField.getUserInput())
        .daysPassed(daysPassedField.getUserInput())
        .optionalRecipe(recipeInputField.getUserInput());
    for (var mealFactField : mealFactFields) {
      builder.addFact(mealFactField.getUserInput());
    }
    return of(builder.create());
  }
}
