package guittests.helpers;

import static guittests.helpers.TabbedPanes.DATABASE_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.model.Meal;

public class GuiMethods {
  private static final int NUMBER_OF_DATA_COLUMNS = 8;

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
    DialogFixture mealInputDialog = window.dialog();
    mealInputDialog.textBox("InputFieldNonemptyTextName")
        .enterText(meal.getName());
    mealInputDialog.comboBox("InputFieldComboBoxCookingTime")
        .selectItem(meal.getCookingTime().toString());
    mealInputDialog.comboBox("InputFieldComboBoxSidedish")
        .selectItem(meal.getSidedish().toString());
    mealInputDialog.comboBox("InputFieldComboBoxObligatoryUtensil")
        .selectItem(meal.getObligatoryUtensil().toString());
    mealInputDialog.comboBox("InputFieldComboBoxCookingPreference")
        .selectItem(meal.getCookingPreference().toString());
    mealInputDialog.textBox("InputFieldNonnegativeIntegerDaysPassed")
        .enterText(meal.getDaysPassed().toString());
    mealInputDialog.textBox("InputFieldTextComment")
        .enterText(meal.getComment());
    // enter recipe
    mealInputDialog.button("ButtonPanelMealInput0").click();
    mealInputDialog.button("ButtonPanelMealInput1").click();
  }

  public void compareDatabaseInTable(List<Meal> meals) {
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    JTableFixture databaseTable = window.table().requireColumnCount(NUMBER_OF_DATA_COLUMNS)
        .requireRowCount(meals.size());
    databaseTable.requireContents(mealsToTableContent(meals));
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
      content[i][6] = meal.getComment();
      content[i][7] = meal.getRecipe().isPresent()
          ? BUNDLES.message("editRecipeButtonLabel")
          : BUNDLES.message("createRecipeButtonLabel");
    }
    return content;
  }
}
