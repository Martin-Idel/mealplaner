package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.io.xml.adapters.MealAdapter.convertMealToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllIngredients;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;

public class MealAdapterTest {

  @Test
  public void adapterTest() {
    MealplanerData mealPlan = setupMealplanerDataWithAllIngredients();

    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();

    Meal convertedMeals1 = convertMealFromXml(mealPlan,
        convertMealToXml(meal1));
    Meal convertedMeals2 = convertMealFromXml(mealPlan,
        convertMealToXml(meal2));

    assertThat(convertedMeals1).isEqualTo(meal1);
    assertThat(convertedMeals2).isEqualTo(meal2);
  }
}
