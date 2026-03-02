// SPDX-License-Identifier: MIT

package etoetests.nativeguitests;

import static etoetests.CommonFunctions.getMeal3;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import etoetests.guitests.constants.ComponentNames;
import etoetests.guitests.helpers.MealplanerTestCase;
import mealplaner.model.meal.Meal;

public class DatabaseEditRemoveMealsTest extends MealplanerTestCase {
  public DatabaseEditRemoveMealsTest() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void removeMealsWorksWithMultipleMeals() throws Exception {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal3());

    windowHelpers.getMealsPane()
        .removeSelectedMeals(0, 1)
        .compareDatabaseInTable(meals);

    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_CANCEL);
    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_SAVE);
  }
}
