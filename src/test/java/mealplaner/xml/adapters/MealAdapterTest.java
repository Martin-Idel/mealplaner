package mealplaner.xml.adapters;

import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.adapters.MealAdapter.convertMealToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import org.junit.Test;

import mealplaner.model.Meal;

public class MealAdapterTest {

  @Test
  public void adapterTest() {
    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();

    Meal convertedMeals1 = convertMealFromXml(
        convertMealToXml(meal1));
    Meal convertedMeals2 = convertMealFromXml(
        convertMealToXml(meal2));

    assertThat(convertedMeals1).isEqualTo(meal1);
    assertThat(convertedMeals2).isEqualTo(meal2);
  }
}
