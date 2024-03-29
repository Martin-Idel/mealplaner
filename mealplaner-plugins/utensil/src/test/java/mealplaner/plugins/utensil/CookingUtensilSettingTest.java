// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static mealplaner.plugins.utensil.proposal.CookingUtensilSetting.createCookingUtensilSettings;
import static mealplaner.plugins.utensil.proposal.CookingUtensilSetting.from;
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
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.utensil.proposal.CookingUtensilSetting;
import mealplaner.plugins.utensil.settingextension.CasseroleSettings;
import testcommons.PluginsUtils;

@ExtendWith(MockitoExtension.class)
class CookingUtensilSettingTest {

  @Mock
  private Set<ObligatoryUtensil> prohibitedUtensil;

  private CookingUtensilSetting utensilSetting;

  @BeforeEach
  void setUp() {
    PluginsUtils.setupMessageBundles(new ObligatoryUtensilPlugin());
    utensilSetting = from(prohibitedUtensil);
  }

  @Test
  void setCasseroleSettingsNone() {
    utensilSetting.setCasseroleSettings(CasseroleSettings.NONE);

    verify(prohibitedUtensil).add(ObligatoryUtensil.CASSEROLE);
    verify(prohibitedUtensil, never()).add(ObligatoryUtensil.PAN);
    verify(prohibitedUtensil, never()).add(ObligatoryUtensil.POT);
  }

  @Test
  void setCasseroleSettingsOnly() {
    utensilSetting.setCasseroleSettings(CasseroleSettings.ONLY);

    verify(prohibitedUtensil).addAll(any());
    verify(prohibitedUtensil).remove(ObligatoryUtensil.CASSEROLE);
  }

  @Test
  void setNumberOfPeopleProhibitsPanForMany() {
    utensilSetting.setNumberOfPeople(nonNegative(5));

    verify(prohibitedUtensil).add(ObligatoryUtensil.PAN);
  }

  @Test
  void reset() {
    Set<ObligatoryUtensil> prohibitedUtensil = new HashSet<>();
    utensilSetting = from(prohibitedUtensil);
    utensilSetting.setCasseroleSettings(CasseroleSettings.NONE);

    utensilSetting.reset();

    assertTrue(prohibitedUtensil.isEmpty());
  }

  @Test
  void prohibit() {
    utensilSetting = createCookingUtensilSettings();
    Meal meal = meal().name("test").addFact(new ObligatoryUtensilFact(POT)).create();
    Meal mealPan = meal().name("test").addFact(new ObligatoryUtensilFact(PAN)).create();
    utensilSetting.setNumberOfPeople(nonNegative(4));

    assertFalse(utensilSetting.prohibits(meal));
    assertTrue(utensilSetting.prohibits(mealPan));
  }

  @Test
  void isUtensilProhibited() {
    utensilSetting = createCookingUtensilSettings();
    utensilSetting.setNumberOfPeople(nonNegative(5));

    assertTrue(utensilSetting.isUtensilProhibited(ObligatoryUtensil.PAN));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.POT));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.CASSEROLE));
  }
}
