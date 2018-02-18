package guittests;

import static mealplaner.model.MealBuilder.from;
import static mealplaner.model.enums.CookingTime.SHORT;
import static org.assertj.swing.data.TableCell.row;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getRecipe2;

import java.util.ArrayList;
import java.util.List;

import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import guittests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Recipe;

public class DatabaseEditTest extends AssertJMealplanerTestCase {

  @Test
  public void addMealAddsMealsInCorrectOrder() {
    List<Meal> meals = new ArrayList<>();
    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();
    meals.add(meal1);
    meals.add(meal2);
    windowHelpers.addMealFromDatabase(meal2);
    windowHelpers.addMealFromDatabase(meal1);
    windowHelpers.compareDatabaseInTable(meals);
    window.button("ButtonPanelDatabaseEdit2").isEnabled();
    window.button("ButtonPanelDatabaseEdit3").isEnabled();
    window.close();
  }

  @Test
  public void removeMealsWorksWithMultipleMeals() {
    List<Meal> meals = new ArrayList<>();
    Meal meal3 = getMeal3();
    meals.add(meal3);
    windowHelpers.addMealFromDatabase(getMeal1());
    windowHelpers.addMealFromDatabase(getMeal2());
    windowHelpers.addMealFromDatabase(meal3);
    window.table().selectRows(0, 1);
    window.button("ButtonPanelDatabaseEdit1").click();
    windowHelpers.compareDatabaseInTable(meals);
    window.button("ButtonPanelDatabaseEdit2").isEnabled();
    window.button("ButtonPanelDatabaseEdit3").isEnabled();
    window.close();
  }

  @Test
  public void canChangeAllAspectsOfAMeal() {
    Meal meal1 = getMeal1();
    Recipe recipe = getRecipe2();
    windowHelpers.addMealFromDatabase(meal1);
    JTableFixture table = window.table();
    table.cell(row(0).column(1)).enterValue(SHORT.toString());
    table.cell(row(0).column(6)).enterValue("New comment");
    table.cell(row(0).column(7)).click();
    windowHelpers.enterRecipe(recipe, window.dialog());
    Meal newMeal = from(meal1)
        .cookingTime(SHORT)
        .comment("New comment")
        .recipe(recipe)
        .create();
    List<Meal> meals = new ArrayList<>();
    meals.add(newMeal);
    windowHelpers.compareDatabaseInTable(meals);
    window.close();
  }
}
