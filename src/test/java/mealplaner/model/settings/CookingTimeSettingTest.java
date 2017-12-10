package mealplaner.model.settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingTime;
import testcommons.BundlesInitialization;

public class CookingTimeSettingTest {
  @Rule
  public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

  private Set<CookingTime> prohibitedTime;
  private CookingTimeSetting cookingTimeSetting;

  @Before
  public void setup() {
    prohibitedTime = new HashSet<>();
    cookingTimeSetting = new CookingTimeSetting(prohibitedTime);
  }

  @Test
  public void prohibitCookingTime() {
    Meal mealLong = mock(Meal.class);
    when(mealLong.getCookingTime()).thenReturn(CookingTime.LONG);
    Meal mealShort = mock(Meal.class);
    when(mealShort.getCookingTime()).thenReturn(CookingTime.SHORT);

    cookingTimeSetting.prohibitCookingTime(CookingTime.SHORT);

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
