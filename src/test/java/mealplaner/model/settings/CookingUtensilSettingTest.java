package mealplaner.model.settings;

import static mealplaner.model.settings.CookingUtensilSetting.createCookingUtensilSettings;
import static mealplaner.model.settings.CookingUtensilSetting.from;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.model.Meal;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.ObligatoryUtensil;

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
    utensilSetting.setNumberOfPeople(5);

    verify(prohibitedUtensil).add(ObligatoryUtensil.PAN);
  }

  @Test
  public void setNumberOfPeopleAllowsPanForFew() {
    utensilSetting.setNumberOfPeople(2);

    verify(prohibitedUtensil).remove(ObligatoryUtensil.PAN);
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
    Meal meal = mock(Meal.class);
    when(meal.getObligatoryUtensil()).thenReturn(ObligatoryUtensil.POT);
    Meal mealPan = mock(Meal.class);
    when(mealPan.getObligatoryUtensil()).thenReturn(ObligatoryUtensil.PAN);
    utensilSetting.setNumberOfPeople(4);

    assertFalse(utensilSetting.prohibits(meal));
    assertTrue(utensilSetting.prohibits(mealPan));
  }

  @Test
  public void isUtensilProhibited() {
    utensilSetting = createCookingUtensilSettings();
    utensilSetting.setNumberOfPeople(5);

    assertTrue(utensilSetting.isUtensilProhibited(ObligatoryUtensil.PAN));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.POT));
    assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.CASSEROLE));
  }
}