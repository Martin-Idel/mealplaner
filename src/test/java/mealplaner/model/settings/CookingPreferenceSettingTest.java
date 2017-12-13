package mealplaner.model.settings;

import static mealplaner.model.MealBuilder.meal;
import static mealplaner.model.enums.CookingPreference.RARE;
import static mealplaner.model.enums.CookingPreference.VERY_POPULAR;
import static mealplaner.model.enums.PreferenceSettings.RARE_NONE;
import static mealplaner.model.settings.CookingPreferenceSetting.createCookingPreferenceSettings;
import static mealplaner.model.settings.CookingPreferenceSetting.from;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;

@RunWith(MockitoJUnitRunner.class)
public class CookingPreferenceSettingTest {

  @Mock
  private Set<CookingPreference> prohibitedpreference;

  private CookingPreferenceSetting utensilSetting;

  @Before
  public void setup() {
    utensilSetting = from(prohibitedpreference);
  }

  @Test
  public void setCookingPreference() {
    utensilSetting.setCookingPreferences(PreferenceSettings.RARE_NONE);

    verify(prohibitedpreference).add(CookingPreference.RARE);
    verify(prohibitedpreference, never()).add(CookingPreference.NO_PREFERENCE);
    verify(prohibitedpreference, never()).add(CookingPreference.VERY_POPULAR);
  }

  @Test
  public void setCasseroleSettingsOnly() {
    utensilSetting.setCookingPreferences(PreferenceSettings.VERY_POPULAR_ONLY);

    verify(prohibitedpreference).addAll(any());
    verify(prohibitedpreference).remove(CookingPreference.VERY_POPULAR);
  }

  @Test
  public void reset() {
    Set<CookingPreference> prohibitedUtensil = new HashSet<>();
    utensilSetting = from(prohibitedUtensil);
    utensilSetting.setCookingPreferences(PreferenceSettings.RARE_NONE);

    utensilSetting.reset();

    assertTrue(prohibitedUtensil.isEmpty());
  }

  @Test
  public void prohibit() {
    utensilSetting = createCookingPreferenceSettings();
    Meal rareMeal = meal().name("test").cookingPreference(RARE).create();
    Meal veryPopularMeal = meal().name("test").cookingPreference(VERY_POPULAR).create();
    utensilSetting.setCookingPreferences(RARE_NONE);

    assertTrue(utensilSetting.prohibits(rareMeal));
    assertFalse(utensilSetting.prohibits(veryPopularMeal));
  }
}
