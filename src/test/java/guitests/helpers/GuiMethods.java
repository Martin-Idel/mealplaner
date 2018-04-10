package guitests.helpers;

import static guitests.helpers.TabbedPanes.DATABASE_EDIT;
import static guitests.helpers.TabbedPanes.PROPOSAL_SUMMARY;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.model.meal.enums.CookingTime.LONG;
import static mealplaner.model.meal.enums.CookingTime.MEDIUM;
import static mealplaner.model.meal.enums.CookingTime.SHORT;
import static mealplaner.model.meal.enums.CookingTime.VERY_SHORT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;
import static org.assertj.swing.data.TableCell.row;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.subsettings.CookingTimeSetting;

public final class GuiMethods {
  private static final int NUMBER_OF_DATA_COLUMNS = 9;
  private static final int RECIPE_COLUMN_IN_DATA_BASE = NUMBER_OF_DATA_COLUMNS - 1;
  private static final int NUMBER_OF_INGREDIENT_COLUMNS = 3;
  private static final int NUMBER_OF_DEFAULT_SETTINGS_COLUMNS = 9;
  private static final int NUMBER_OF_WEEKDAYS = 7;

  private final FrameFixture window;

  private GuiMethods(FrameFixture window) {
    this.window = window;
  }

  public static GuiMethods create(FrameFixture window) {
    return new GuiMethods(window);
  }

  public void enterMealFromMenu(Meal meal) {
    window.menuItem("MenuFile").click();
    window.menuItem("MenuItemCreateMeal").click();
    DialogFixture mealInputDialog = addMeal(meal);
    mealInputDialog.button("ButtonPanelMealInput1").click();
  }

  public void enterRecipe(Recipe recipe, DialogFixture dialog) {
    dialog.textBox("InputFieldNonnegativeIntegerRecipePortions")
        .enterText(recipe.getNumberOfPortions().toString());
    JTableFixture ingredientsTable = dialog.table();
    List<QuantitativeIngredient> ingredientsAsIs = recipe.getIngredientListAsIs();
    for (int i = 0; i < ingredientsAsIs.size(); i++) {
      QuantitativeIngredient ingredient = ingredientsAsIs.get(i);
      ingredientsTable.enterValue(row(i).column(0), ingredient.getIngredient().getName());
      ingredientsTable.enterValue(row(i).column(1), ingredient.getAmount().toString());
    }
    dialog.button("ButtonPanelRecipeInput1").click();
  }

  public void addMealFromDatabase(Meal meal) {
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    window.button("ButtonPanelDatabaseEdit0").click();
    addMeal(meal);
  }

  public void compareDatabaseInTable(List<Meal> meals) {
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    JTableFixture databaseTable = window.table().requireColumnCount(NUMBER_OF_DATA_COLUMNS)
        .requireRowCount(meals.size());
    databaseTable.requireContents(mealsToTableContent(meals));
    assertCorrectRecipes(meals, databaseTable);
  }

  public void enterDefaultSettings(DefaultSettings defaultSettings) {
    inDefaultSettingsDialog(settingsTable -> enterDefaultSettings(defaultSettings, settingsTable));
  }

  public void compareDefaultSettings(DefaultSettings defaultSettings) {
    inDefaultSettingsDialog(settingsTable -> settingsTable
        .requireContents(defaultSettingsTableEntries(defaultSettings)));
  }

  private void enterDefaultSettings(DefaultSettings defaultSettings, JTableFixture settingsTable) {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettings();
    for (DayOfWeek day : DayOfWeek.values()) {
      enterSetting(settingsTable, settings.get(day), day.getValue() - 1, false);
    }
  }

  private void inDefaultSettingsDialog(Consumer<JTableFixture> doInSettingsTable) {
    window.tabbedPane().selectTab(PROPOSAL_SUMMARY.number());
    window.button("ButtonProposalSummaryDefaultSettings").click();
    DialogFixture settingsDialog = window.dialog();
    JTableFixture settingsTable = settingsDialog.table();
    settingsTable.requireRowCount(NUMBER_OF_WEEKDAYS);
    settingsTable.requireColumnCount(NUMBER_OF_DEFAULT_SETTINGS_COLUMNS);
    doInSettingsTable.accept(settingsTable);
    settingsDialog.button("ButtonPanelDefaultSettingsInput1");
  }

  public void enterSetting(JTableFixture settingsTable, Settings setting, int dayNumber) {
    enterSetting(settingsTable, setting, dayNumber, true);
  }

  public void updateToToday() {
    window.tabbedPane().selectTab(PROPOSAL_SUMMARY.number());
    window.button("ButtonProposalSummaryUpdate").click();
    DialogFixture dialog = window.dialog();
    dialog.button("ButtonPanelUpdatePastMeals1").click();
  }

  private void enterSetting(JTableFixture settingsTable, Settings setting, int dayNumber,
      boolean proposal) {
    int firstColumn = proposal ? 2 : 1;
    CookingTimeSetting timeSetting = setting.getCookingTime();
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, VERY_SHORT);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, SHORT);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, MEDIUM);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, LONG);
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getNumberOfPeople().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getCasserole().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getPreference().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getCourseSettings().toString());
  }

  private void updateCheckBox(JTableFixture settingsTable,
      TableCell checkbox,
      CookingTimeSetting cookingTime,
      CookingTime time) {
    if (checkBoxNeedsUpdate(settingsTable, checkbox, cookingTime, time)) {
      settingsTable.click(checkbox, LEFT_BUTTON);
    }
  }

  private void updateComboBox(JTableFixture settingsTable,
      TableCell cell,
      String value) {
    if (!settingsTable.cell(cell).value().equals(value)) {
      settingsTable.enterValue(cell, value);
    }
  }

  private boolean checkBoxNeedsUpdate(JTableFixture settingsTable,
      TableCell checkbox,
      CookingTimeSetting cookingTime,
      CookingTime time) {
    return !cookingTime.contains(time) != settingsTable.valueAt(checkbox)
        .equals(Boolean.toString(true));
  }

  private String[][] defaultSettingsTableEntries(DefaultSettings defaultSettings) {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettings();
    String[][] content = new String[NUMBER_OF_WEEKDAYS][NUMBER_OF_DEFAULT_SETTINGS_COLUMNS];
    for (DayOfWeek day : DayOfWeek.values()) {
      Settings setting = settings.get(day);
      int row = day.getValue() - 1;
      content[row][0] = day.getDisplayName(FULL, BUNDLES.locale());
      content[row][1] = Boolean.toString(!setting.getCookingTime().contains(VERY_SHORT));
      content[row][2] = Boolean.toString(!setting.getCookingTime().contains(SHORT));
      content[row][3] = Boolean.toString(!setting.getCookingTime().contains(MEDIUM));
      content[row][4] = Boolean.toString(!setting.getCookingTime().contains(LONG));
      content[row][5] = setting.getNumberOfPeople().toString();
      content[row][6] = setting.getCasserole().toString();
      content[row][7] = setting.getPreference().toString();
      content[row][8] = setting.getCourseSettings().toString();
    }
    return content;
  }

  private DialogFixture addMeal(Meal meal) {
    DialogFixture mealInputDialog = window.dialog();
    mealInputDialog.textBox("InputFieldNonemptyTextName")
        .enterText(meal.getName());
    mealInputDialog.comboBox("InputFieldComboBoxCookingTime")
        .selectItem(meal.getCookingTime().toString());
    mealInputDialog.comboBox("InputFieldComboBoxSidedish")
        .selectItem(meal.getSidedish().toString());
    mealInputDialog.comboBox("InputFieldComboBoxObligatoryUtensil")
        .selectItem(meal.getObligatoryUtensil().toString());
    mealInputDialog.textBox("InputFieldNonnegativeIntegerDaysPassed")
        .enterText(meal.getDaysPassed().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCookingPreference")
        .selectItem(meal.getCookingPreference().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCourseType")
        .selectItem(meal.getCourseType().toString());
    mealInputDialog.textBox("InputFieldTextComment")
        .enterText(meal.getComment());
    if (meal.getRecipe().isPresent()) {
      mealInputDialog.button("InputFieldButtonRecipe").click();
      enterRecipe(meal.getRecipe().get(), mealInputDialog);
    }
    mealInputDialog.button("ButtonPanelMealInput0").click();
    return mealInputDialog;
  }

  private void assertCorrectRecipes(List<Meal> meals, JTableFixture databaseTable) {
    for (int i = 0; i < meals.size(); i++) {
      if (meals.get(i).getRecipe().isPresent()) {
        Recipe recipe = meals.get(i).getRecipe().get();
        databaseTable.cell(row(i).column(RECIPE_COLUMN_IN_DATA_BASE)).click();
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
    recipeDialog.button("ButtonPanelRecipeInput0").click();
  }

  private String[][] recipeToTableContent(Recipe recipe) {
    List<QuantitativeIngredient> ingredients = recipe
        .getIngredientListAsIs();
    ingredients.sort((ingredient1, ingredient2) -> ingredient1.getIngredient().getName()
        .compareTo(ingredient2.getIngredient().getName()));
    String[][] content = new String[ingredients.size() + 1][NUMBER_OF_INGREDIENT_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      QuantitativeIngredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getIngredient().getName();
      content[i][1] = ingredient.getAmount().toString();
      content[i][2] = ingredient.getIngredient().getMeasure().toString();
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
      content[i][1] = meal.getCookingTime().toString();
      content[i][2] = meal.getSidedish().toString();
      content[i][3] = meal.getObligatoryUtensil().toString();
      content[i][4] = meal.getDaysPassed().toString();
      content[i][5] = meal.getCookingPreference().toString();
      content[i][6] = meal.getCourseType().toString();
      content[i][7] = meal.getComment();
      content[i][8] = meal.getRecipe().isPresent()
          ? BUNDLES.message("editRecipeButtonLabel")
          : BUNDLES.message("createRecipeButtonLabel");
    }
    return content;
  }
}
