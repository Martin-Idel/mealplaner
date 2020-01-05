// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.PAN;
import static mealplaner.plugins.utensil.mealextension.ObligatoryUtensil.POT;
import static mealplaner.plugins.utensil.proposal.CookingUtensilSetting.createCookingUtensilSettings;
import static mealplaner.plugins.utensil.proposal.CookingUtensilSetting.from;
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

import mealplaner.model.meal.Meal;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensil;
import mealplaner.plugins.utensil.mealextension.ObligatoryUtensilFact;
import mealplaner.plugins.utensil.proposal.CookingUtensilSetting;
import mealplaner.plugins.utensil.settingextension.CasseroleSettings;

@RunWith(MockitoJUnitRunner.class)
public class CookingUtensilSettingTest {

  @Mock
  private Set<ObligatoryUtensil> prohibitedUtensil;

  private CookingUtensilSetting utensilSetting;

  @Before
  public void setup() {
    utensilSetting = from(prohibitedUtensil);
  }

  @Test
  public void setCasseroleSettingsNone() {
    utensilSetting.setCasseroleSettings(CasseroleSettings.NONE);

    verify(prohibitedUtensil).add(ObligatoryUtensil.CASSEROLE);
    verify(prohibitedUtensil, never()).add(ObligatoryUtensil.PAN);
    verify(prohibitedUtensil, never()).add(ObligatoryUtensil.POT);
  }

  @Test
  public void setCasseroleSettingsOnly() {
    utensilSetting.setCasseroleSettings(CasseroleSettings.ONLY);

    verify(prohibitedUtensil).addAll(any());
    verify(prohibitedUtensil).remove(ObligatoryUtensil.CASSEROLE);
  }

  @Test
  public void setNumberOfPeopleProhibitsPanForMany() {
    utensilSetting.setNumberOfPeople(nonNegative(5));

    verify(prohibitedUtensil).add(ObligatoryUtensil.PAN);
  }

  @Test
  public void reset() {
    Set<ObligatoryUtensil> prohibitedUtensil = new HashSet<>();
    utensilSetting = from(prohibitedUtensil);
    utensilSetting.setCasseroleSettings(CasseroleSettings.NONE);

    utensilSetting.reset();

    assertTrue(prohibitedUtensil.isEmpty());
  }

  @Test
  public void prohibit() {
    utensilSetting = createCookingUtensilSettings();
    Meal meal = meal().name("test").addFact(new ObligatoryUtensilFact(POT)).create();
    Meal mealPan = meal().name("test").addFact(new ObligatoryUtensilFact(PAN)).create();
    utensilSetting.setNumberOfPeople(nonNegative(4));

    assertFalse(utensilSetting.prohibits(meal));
    assertTrue(utensilSetting.prohibits(mealPan));
  }

  @Test
  public void isUtensilProhibited() {
    utensilSetting = createCookingUtensilSettings();
    utensilSetting.setNumberOfPeople(nonNegative(5));

    assertTrue(utensilSetting.isUtensilProhibited(ObligatoryUtensil.PAN));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.POT));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.CASSEROLE));
  }
}