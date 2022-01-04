// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto;

import static mealplaner.commons.NonnegativeInteger.ONE;
import static mealplaner.model.meal.MealBuilder.meal;
import static testcommons.PluginsXmlUtils.assertSaveAndReloadMealWorksCorrectly;

import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.recipephoto.mealedit.RecipePhotoFact;
import testcommons.PluginsUtils;
import testcommons.XmlInteraction;

public class RecipePhotoFactXmlTest extends XmlInteraction {
  @BeforeEach
  public void setUp() {
    PluginsUtils.setupMessageBundles(new RecipePhotoPlugin());
  }

  @Test
  public void roundTripWithPhotoFactCanBeSavedCorrectly() {
    Meal meal = meal()
        .name("Test1")
        .daysPassed(ONE)
        .addFact(new RecipePhotoFact(Paths.get(System.getProperty("user.home"), ".mealplaner", "photos", "test")))
        .create();

    assertSaveAndReloadMealWorksCorrectly(meal, DESTINATION_FILE_PATH, new RecipePhotoPlugin());
  }
}
