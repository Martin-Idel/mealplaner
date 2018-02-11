package guittests;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import guittests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class MenuItemsTest extends AssertJMealplanerTestCase {

  @Test
  public void pressExitButton() {
    Meal meal1 = createMeal("Bla", CookingTime.LONG, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
        CookingPreference.RARE, nonNegative(2), "No comment", Optional.empty());
    windowHelpers.enterMealFromMenu(meal1);
    List<Meal> meals = new ArrayList<>();
    meals.add(meal1);
    windowHelpers.compareDatabaseInTable(meals);
    window.close();
  }
}
