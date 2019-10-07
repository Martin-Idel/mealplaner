// SPDX-License-Identifier: MIT

package mealplaner.model.settings;

import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.plugins.cookingtime.settingextension.CookingTimeSubSetting;

public class CookingTimeSubSettingTest {

  private Set<CookingTime> prohibitedTime;
  private CookingTimeSubSetting cookingTimeSubSetting;

  @Before
  public void setup() {
    prohibitedTime = new HashSet<>();
    cookingTimeSubSetting = new CookingTimeSubSetting(prohibitedTime);
  }

  @Test
  public void prohibitCookingTime() {
    Meal mealLong = meal().name("test").cookingTime(LONG).create();
    Meal mealShort = meal().name("test").cookingTime(SHORT).create();

    var newCookingTimeSetting = cookingTimeSubSetting.prohibitCookingTime(SHORT);

    assertTrue(newCookingTimeSetting.prohibits(mealShort));
    assertFalse(newCookingTimeSetting.prohibits(mealLong));
  }

  @Test
  public void isTimeProhibited() {
    var newCookingTimeSetting = cookingTimeSubSetting.prohibitCookingTime(CookingTime.MEDIUM);

    assertTrue(newCookingTimeSetting.isTimeProhibited(CookingTime.MEDIUM));
    assertFalse(newCookingTimeSetting.isTimeProhibited(CookingTime.SHORT));
    assertFalse(newCookingTimeSetting.isTimeProhibited(CookingTime.LONG));
  }
}
