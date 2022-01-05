// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;
import mealplaner.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import testcommons.PluginsUtils;

class CookingTimeSubSettingTest {
  private CookingTimeSubSetting cookingTimeSubSetting;

  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new CookingTimePlugin());
    cookingTimeSubSetting = new CookingTimeSubSetting(new HashSet<>());
  }

  @Test
  void prohibitCookingTime() {
    Meal mealLong = meal().name("test").addFact(new CookingTimeFact(LONG)).create();
    Meal mealShort = meal().name("test").addFact(new CookingTimeFact(SHORT)).create();

    var newCookingTimeSetting = cookingTimeSubSetting.prohibitCookingTime(SHORT);

    assertTrue(newCookingTimeSetting.prohibits(mealShort));
    assertFalse(newCookingTimeSetting.prohibits(mealLong));
  }

  @Test
  void isTimeProhibited() {
    var newCookingTimeSetting = cookingTimeSubSetting.prohibitCookingTime(CookingTime.MEDIUM);

    assertTrue(newCookingTimeSetting.isTimeProhibited(CookingTime.MEDIUM));
    assertFalse(newCookingTimeSetting.isTimeProhibited(CookingTime.SHORT));
    assertFalse(newCookingTimeSetting.isTimeProhibited(CookingTime.LONG));
  }
}
