package guitests.databaseedit;

import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import guitests.helpers.MealsEditPageObject;
import mealplaner.model.meal.Meal;

public class DatabaseEditAddMealsTest extends AssertJMealplanerTestCase {

  @Test
  public void addMealAddsMealsInCorrectOrder() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    MealsEditPageObject database = windowHelpers.getMealsPane()
        .addMeal(getMeal2())
        .addMeal(getMeal1())
        .compareDatabaseInTable(meals);
    database.cancelButton().requireEnabled();
    database.saveButton().requireEnabled();
  }
}
