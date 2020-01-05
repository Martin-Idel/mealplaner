// SPDX-License-Identifier: MIT

package etoetests.guitests.databaseedit;

import static etoetests.CommonFunctions.getMeal3;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import etoetests.guitests.helpers.AssertJMealplanerTestCase;
import etoetests.guitests.pageobjects.MealsEditPageObject;
import mealplaner.model.meal.Meal;

public class DatabaseEditRemoveMealsTest extends AssertJMealplanerTestCase {
  public DatabaseEditRemoveMealsTest() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void removeMealsWorksWithMultipleMeals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal3());

    MealsEditPageObject database = windowHelpers.getMealsPane()
        .removeSelectedMeals(0, 1)
        .compareDatabaseInTable(meals);
    database.cancelButton().requireEnabled();
    database.saveButton().requireEnabled();
  }
}
