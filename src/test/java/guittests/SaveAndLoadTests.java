package guittests;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.IngredientProviderIoGui.loadIngredientProvider;
import static mealplaner.model.Meal.createMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTableFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.gui.MainGui;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.provider.IngredientProvider;

public class SaveAndLoadTests extends AssertJSwingJUnitTestCase {
  private static final int NUMBER_OF_DATA_COLUMNS = 8;
  private static final int DATABASE_PANE = 1; // into enum

  private FrameFixture window;

  @Override
  protected void onSetUp() {
    MainGui frame = GuiActionRunner.execute(() -> createMainApplication());
    window = new FrameFixture(robot(), frame.getFrame());
    window.show();
  }

  private MainGui createMainApplication() {
    MealplanerData data = new MealplanerData();
    IngredientProvider ingredientProvider = loadIngredientProvider();
    JFrame mainFrame = new JFrame(BUNDLES.message("mainFrameTitle"));
    DialogFactory dialogFactory = new DialogFactory(mainFrame);
    return new MainGui(mainFrame, data, dialogFactory, ingredientProvider);
  }

  @Test
  public void pressExitButton() {
    window.tabbedPane().selectTab(DATABASE_PANE).click();
    Meal meal1 = createMeal("Bla", CookingTime.LONG, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
        CookingPreference.RARE, nonNegative(2), "No comment", Optional.empty());
    enterMeal(meal1);
    List<Meal> meals = new ArrayList<>();
    meals.add(meal1);
    compareTable(meals);
    window.close();
  }

  private void enterMeal(Meal meal) {
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

  private void compareTable(List<Meal> meals) {
    window.tabbedPane().selectTab(DATABASE_PANE).click();
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
