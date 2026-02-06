// SPDX-License-Identifier: MIT

package etoetests.nativeguitests;

import static etoetests.CommonFunctions.getMeal3;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import etoetests.guitests.helpers.NonAssertJMealplanerTestCase;
import mealplaner.model.meal.Meal;

public class DatabaseEditRemoveMealsTestNative extends NonAssertJMealplanerTestCase {
  public DatabaseEditRemoveMealsTestNative() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void removeMealsWorksWithMultipleMeals() throws Exception {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal3());

    windowHelpersNative.getMealsPane()
        .removeSelectedMeals(0, 1)
        .compareDatabaseInTable(meals);
    
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getMealsPane().cancelButton().doClick();
    });
    Thread.sleep(500);
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(windowHelpersNative.getMealsPane().cancelButton().isEnabled()).isFalse();
      assertThat(windowHelpersNative.getMealsPane().saveButton().isEnabled()).isFalse();
    });
  }
}