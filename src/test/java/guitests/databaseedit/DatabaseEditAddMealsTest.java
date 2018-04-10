package guitests.databaseedit;

import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.meal.Meal;

public class DatabaseEditAddMealsTest extends AssertJMealplanerTestCase {

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
    window.button("ButtonPanelDatabaseEdit2").requireEnabled();
    window.button("ButtonPanelDatabaseEdit3").requireEnabled();
    window.close();
  }
}
