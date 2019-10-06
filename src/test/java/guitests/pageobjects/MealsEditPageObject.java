// SPDX-License-Identifier: MIT

package guitests.pageobjects;

import static guitests.pageobjects.IngredientsEditPageObject.enterIngredient;
import static guitests.pageobjects.TabbedPanes.DATABASE_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.data.TableCell.row;

import java.util.Comparator;
import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.plugins.builtins.courses.CourseType;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.plugins.sidedish.mealextension.Sidedish;
import mealplaner.plugins.plugins.sidedish.mealextension.SidedishFact;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.plugins.utensil.mealextension.ObligatoryUtensilFact;

public class MealsEditPageObject {
  private static final int NUMBER_OF_DATA_COLUMNS = 9;
  private static final int NUMBER_OF_INGREDIENT_COLUMNS = 3;

  private static final int DATABASE_NAME_COLUMN = 0;
  private static final int DATABASE_TIME_COLUMN = 1;
  private static final int DATABASE_SIDEDISH_COLUMN = 2;
  private static final int DATABASE_OBLIGATORY_UTENSIL_COLUMN = 3;
  private static final int DATABASE_DAYS_LAST_COOKED_COLUMN = 4;
  private static final int DATABASE_MEAL_PREFERENCE_COLUMN = 5;
  private static final int DATABASE_COURSE_TYPE_COLUMN = 6;
  private static final int DATABASE_COMMENT_COLUMN = 7;
  private static final int DATABASE_RECIPE_COLUMN = 8;

  private final FrameFixture window;

  MealsEditPageObject(FrameFixture window) {
    this.window = window;
  }

  public MealsEditPageObject compareDatabaseInTable(List<Meal> meals) {
    JTableFixture databaseTable = window.table()
        .requireColumnCount(NUMBER_OF_DATA_COLUMNS)
        .requireRowCount(meals.size());
    databaseTable.requireContents(mealsToTableContent(meals));
    assertCorrectRecipes(meals, databaseTable);
    return this;
  }

  public MealsEditPageObject clickCancelButtonMakingSureItIsEnabled() {
    cancelButton().requireEnabled();
    saveButton().requireEnabled();
    cancelButton().click();
    cancelButton().requireDisabled();
    saveButton().requireDisabled();
    return this;
  }

  public MealsEditPageObject addMeal(Meal meal, Ingredient... missingIngredients) {
    addMealButton().click();
    addMealInDialog(meal, missingIngredients);
    return this;
  }

  public MealsEditPageObject addMealWithIngredients() {
    addMealButton().click();

    return this;
  }

  public MealsEditPageObject removeSelectedMeals(int... rows) {
    JTableFixture databaseTable = window.table()
        .requireColumnCount(NUMBER_OF_DATA_COLUMNS);
    databaseTable.selectRows(rows);
    removeSelectedButton().click();
    return this;
  }

  public MealsEditPageObject changeName(int row, String name) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_NAME_COLUMN)).enterValue(name);
    return this;
  }

  public MealsEditPageObject changeCookingTime(int row, CookingTime cookingTime) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_TIME_COLUMN)).enterValue(cookingTime.toString());
    return this;
  }

  public MealsEditPageObject changeSideDish(int row, Sidedish sidedish) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_SIDEDISH_COLUMN)).enterValue(sidedish.toString());
    return this;
  }

  public MealsEditPageObject changeObligatoryUtensil(int row, ObligatoryUtensil obligatoryUtensil) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_OBLIGATORY_UTENSIL_COLUMN))
        .enterValue(obligatoryUtensil.toString());
    return this;
  }

  public MealsEditPageObject changePreference(int row, CookingPreference cookingPreference) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_MEAL_PREFERENCE_COLUMN))
        .enterValue(cookingPreference.toString());
    return this;
  }

  public MealsEditPageObject changeDaysPassed(int row, NonnegativeInteger daysPassed) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_DAYS_LAST_COOKED_COLUMN))
        .enterValue(Integer.toString(daysPassed.value));
    return this;
  }

  public MealsEditPageObject changeComment(int row, String comment) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_COMMENT_COLUMN)).enterValue(comment);
    return this;
  }

  public MealsEditPageObject changeCourseType(int row, CourseType courseType) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_COURSE_TYPE_COLUMN)).enterValue(courseType.toString());
    return this;
  }

  public MealsEditPageObject enterRecipe(int row, Recipe recipe) {
    JTableFixture table = window.table();
    table.cell(row(row).column(DATABASE_RECIPE_COLUMN)).click();
    enterRecipeInDialog(recipe, window.dialog());
    return this;
  }

  public MealsEditPageObject save() {
    saveButton().click();
    return this;
  }

  private JButtonFixture addMealButton() {
    return window.button("ButtonPanelDatabaseEdit0");
  }

  private JButtonFixture removeSelectedButton() {
    return window.button("ButtonPanelDatabaseEdit1");
  }

  public JButtonFixture saveButton() {
    return window.button("ButtonPanelDatabaseEdit2");
  }

  public JButtonFixture cancelButton() {
    return window.button("ButtonPanelDatabaseEdit3");
  }

  public MealsEditPageObject mealsTabbedPane() {
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    return this;
  }

  public MealsEditPageObject assertRecipeIn(int row, Recipe recipe) {
    window.table().cell(row(row).column(DATABASE_RECIPE_COLUMN)).click();
    compareRecipeInTable(recipe);
    return this;
  }

  private void addMealInDialog(Meal meal, Ingredient... missingIngredients) {
    DialogFixture mealInputDialog = window.dialog();
    mealInputDialog.textBox("InputFieldNonemptyTextName")
        .enterText(meal.getName());
    mealInputDialog.comboBox("InputFieldComboBoxCookingTime")
        .selectItem(meal.getTypedMealFact(CookingTimeFact.class).getCookingTime().toString());
    mealInputDialog.comboBox("InputFieldComboBoxSidedish")
        .selectItem(meal.getTypedMealFact(SidedishFact.class).getSidedish().toString());
    mealInputDialog.comboBox("InputFieldComboBoxObligatoryUtensil")
        .selectItem(meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil().toString());
    mealInputDialog.textBox("InputFieldNonnegativeIntegerDaysPassed")
        .enterText(meal.getDaysPassed().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCookingPreference")
        .selectItem(meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCourseType")
        .selectItem(meal.getTypedMealFact(CourseTypeFact.class).getCourseType().toString());
    mealInputDialog.textBox("InputFieldTextComment")
        .enterText(meal.getTypedMealFact(CommentFact.class).getComment());
    if (meal.getRecipe().isPresent()) {
      mealInputDialog.button("InputFieldButtonRecipe").click();
      enterRecipeInDialog(meal.getRecipe().get(), mealInputDialog, missingIngredients);
    }
    mealInputDialog.button("ButtonPanelMealInput0").click();
  }

  private MealsEditPageObject enterRecipeInDialog(Recipe recipe, DialogFixture recipeDialog,
                                                  Ingredient... ingredients) {
    if (ingredients.length != 0) {
      recipeDialog.button("ButtonPanelRecipeInput0").click();
      for (Ingredient ingredient : ingredients) {
        enterIngredient(ingredient, window.dialog("IngredientsInputDialog"));
      }
    }
    recipeDialog.textBox("InputFieldNonnegativeIntegerRecipePortions")
        .enterText(recipe.getNumberOfPortions().toString());
    JTableFixture ingredientsTable = recipeDialog.table();
    List<QuantitativeIngredient> ingredientsAsIs = recipe.getIngredientListAsIs();
    for (int i = 0; i < ingredientsAsIs.size(); i++) {
      QuantitativeIngredient ingredient = ingredientsAsIs.get(i);
      ingredientsTable.enterValue(row(i).column(0), ingredient.getIngredient().getName());
      ingredientsTable.enterValue(row(i).column(1), ingredient.getAmount().toString());
    }
    recipeDialog.button("ButtonPanelRecipeInput2").click();
    return this;
  }

  private void assertCorrectRecipes(List<Meal> meals, JTableFixture databaseTable) {
    for (int i = 0; i < meals.size(); i++) {
      if (meals.get(i).getRecipe().isPresent()) {
        Recipe recipe = meals.get(i).getRecipe().get();
        databaseTable.cell(row(i).column(DATABASE_RECIPE_COLUMN)).click();
        compareRecipeInTable(recipe);
      }
    }
  }

  private void compareRecipeInTable(Recipe recipe) {
    DialogFixture recipeDialog = window.dialog();
    assertThat(recipeDialog.textBox("InputFieldNonnegativeIntegerRecipePortions").text())
        .isEqualTo(recipe.getNumberOfPortions().toString());
    JTableFixture recipeTable = recipeDialog.table();
    recipeTable.requireColumnCount(NUMBER_OF_INGREDIENT_COLUMNS)
        .requireRowCount(recipe.getIngredientListAsIs().size() + 1);
    recipeTable.requireContents(recipeToTableContent(recipe));
    recipeDialog.button("ButtonPanelRecipeInput1").click();
  }

  private String[][] recipeToTableContent(Recipe recipe) {
    List<QuantitativeIngredient> ingredients = recipe
        .getIngredientListAsIs();
    ingredients.sort(Comparator.comparing(ingredient2 -> ingredient2.getIngredient().getName()));
    String[][] content = new String[ingredients.size() + 1][NUMBER_OF_INGREDIENT_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      QuantitativeIngredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getIngredient().getName();
      content[i][1] = ingredient.getAmount().toString();
      content[i][2] = ingredient.getIngredient().getPrimaryMeasure().toString();
    }
    content[ingredients.size()][0] = "";
    content[ingredients.size()][1] = "0";
    content[ingredients.size()][2] = "-";
    return content;
  }

  private String[][] mealsToTableContent(List<Meal> meals) {
    String[][] content = new String[meals.size()][NUMBER_OF_DATA_COLUMNS];
    for (int i = 0; i < meals.size(); i++) {
      Meal meal = meals.get(i);
      content[i][0] = meal.getName();
      content[i][1] = meal.getTypedMealFact(CookingTimeFact.class).getCookingTime().toString();
      content[i][2] = meal.getTypedMealFact(SidedishFact.class).getSidedish().toString();
      content[i][3] = meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil().toString();
      content[i][4] = meal.getDaysPassed().toString();
      content[i][5] = meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference().toString();
      content[i][6] = meal.getTypedMealFact(CourseTypeFact.class).getCourseType().toString();
      content[i][7] = meal.getTypedMealFact(CommentFact.class).getComment();
      content[i][8] = meal.getRecipe().isPresent()
          ? BUNDLES.message("editRecipeButtonLabel")
          : BUNDLES.message("createRecipeButtonLabel");
    }
    return content;
  }
}
