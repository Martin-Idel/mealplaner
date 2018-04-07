package guitests.databaseedit;

import static guitests.helpers.TabbedPanes.DATABASE_EDIT;
import static testcommons.CommonFunctions.getMeal3;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.Meal;

public class DatabaseEditRemoveMealsTest extends AssertJMealplanerTestCase {
  public DatabaseEditRemoveMealsTest() {
    super("src/test/resources/mealsXmlV2.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void removeMealsWorksWithMultipleMeals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal3());

    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    window.table().selectRows(0, 1);
    window.button("ButtonPanelDatabaseEdit1").click();
    windowHelpers.compareDatabaseInTable(meals);
    window.button("ButtonPanelDatabaseEdit2").requireEnabled();
    window.button("ButtonPanelDatabaseEdit3").requireEnabled();
    window.close();
  }
}
