package mealplaner.plugins.plugins.sidedish;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.PASTA;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;

import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.plugins.sidedish.mealextension.SidedishFact;
import testcommons.XmlInteraction;

public class SideDishXmlTest extends XmlInteraction {
  @Test
  public void roundTripWithCookingTimeCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new SidedishFact(PASTA))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new SideDishPlugin());
  }
}
