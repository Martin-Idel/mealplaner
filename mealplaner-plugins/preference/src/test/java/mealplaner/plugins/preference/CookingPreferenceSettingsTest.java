// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference;

import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.preference.mealextension.CookingPreference.RARE;
import static mealplaner.plugins.preference.mealextension.CookingPreference.VERY_POPULAR;
import static mealplaner.plugins.preference.proposal.CookingPreferenceSettings.createCookingPreferenceSettings;
import static mealplaner.plugins.preference.proposal.CookingPreferenceSettings.from;
import static mealplaner.plugins.preference.settingextension.PreferenceSettings.RARE_NONE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.preference.mealextension.CookingPreference;
import mealplaner.plugins.preference.mealextension.CookingPreferenceFact;
import mealplaner.plugins.preference.proposal.CookingPreferenceSettings;
import mealplaner.plugins.preference.settingextension.PreferenceSettings;
import testcommons.PluginsUtils;

@ExtendWith(MockitoExtension.class)
public class CookingPreferenceSettingsTest {

  @Mock
  private Set<CookingPreference> prohibitedPreference;

  private CookingPreferenceSettings utensilSetting;

  @BeforeEach
  public void setUp() {
    PluginsUtils.setupMessageBundles(new CookingPreferencePlugin());
    utensilSetting = from(prohibitedPreference);
  }

  @Test
  public void setCookingPreference() {
    utensilSetting.setCookingPreferences(PreferenceSettings.RARE_NONE);

    verify(prohibitedPreference).add(CookingPreference.RARE);
    verify(prohibitedPreference, never()).add(CookingPreference.NO_PREFERENCE);
    verify(prohibitedPreference, never()).add(CookingPreference.VERY_POPULAR);
  }

  @Test
  public void setCasseroleSettingsOnly() {
    utensilSetting.setCookingPreferences(PreferenceSettings.VERY_POPULAR_ONLY);

    verify(prohibitedPreference).addAll(any());
    verify(prohibitedPreference).remove(CookingPreference.VERY_POPULAR);
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
    Meal rareMeal = meal().name("test").addFact(new CookingPreferenceFact(RARE)).create();
    Meal veryPopularMeal = meal().name("test").addFact(new CookingPreferenceFact(VERY_POPULAR)).create();
    utensilSetting.setCookingPreferences(RARE_NONE);

    assertTrue(utensilSetting.prohibits(rareMeal));
    assertFalse(utensilSetting.prohibits(veryPopularMeal));
  }
}
