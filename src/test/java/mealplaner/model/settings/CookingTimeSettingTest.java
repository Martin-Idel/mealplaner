package mealplaner.model.settings;

import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.meal.enums.CookingTime.LONG;
import static mealplaner.model.meal.enums.CookingTime.SHORT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.settings.subsettings.CookingTimeSetting;

public class CookingTimeSettingTest {

  private Set<CookingTime> prohibitedTime;
  private CookingTimeSetting cookingTimeSetting;

  @Before
  public void setup() {
    prohibitedTime = new HashSet<>();
    cookingTimeSetting = new CookingTimeSetting(prohibitedTime);
  }

  @Test
  public void prohibitCookingTime() {
    Meal mealLong = meal().name("test").cookingTime(LONG).create();
    Meal mealShort = meal().name("test").cookingTime(SHORT).create();

    cookingTimeSetting.prohibitCookingTime(SHORT);

    assertTrue(cookingTimeSetting.prohibits(mealShort));
    assertFalse(cookingTimeSetting.prohibits(mealLong));
  }

  @Test
  public void reset() {
    cookingTimeSetting.prohibitCookingTime(CookingTime.MEDIUM);

    cookingTimeSetting.reset();

    assertTrue(prohibitedTime.isEmpty());
  }

  @Test
  public void isTimeProhibited() {
    cookingTimeSetting.prohibitCookingTime(CookingTime.MEDIUM);

    assertTrue(cookingTimeSetting.isTimeProhibited(CookingTime.MEDIUM));
    assertFalse(cookingTimeSetting.isTimeProhibited(CookingTime.SHORT));
    assertFalse(cookingTimeSetting.isTimeProhibited(CookingTime.LONG));
  }
}
