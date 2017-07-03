package mealplaner.model.settings;

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
		utensilSetting = new CookingUtensilSetting(prohibitedUtensil);
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
	public void setManySettings() {
		utensilSetting.setManyPeople();

		verify(prohibitedUtensil).add(ObligatoryUtensil.PAN);
	}

	@Test
	public void reset() {
		Set<ObligatoryUtensil> prohibitedUtensil = new HashSet<>();
		utensilSetting = new CookingUtensilSetting(prohibitedUtensil);
		utensilSetting.setCasseroleSettings(CasseroleSettings.NONE);

		utensilSetting.reset();

		assertTrue(prohibitedUtensil.isEmpty());
	}

	@Test
	public void prohibit() {
		utensilSetting = new CookingUtensilSetting();
		Meal meal = mock(Meal.class);
		when(meal.getObligatoryUtensil()).thenReturn(ObligatoryUtensil.POT);
		Meal mealPan = mock(Meal.class);
		when(mealPan.getObligatoryUtensil()).thenReturn(ObligatoryUtensil.PAN);
		utensilSetting.setManyPeople();

		assertFalse(utensilSetting.prohibits(meal));
		assertTrue(utensilSetting.prohibits(mealPan));
	}

	@Test
	public void isUtensilProhibited() {
		utensilSetting = new CookingUtensilSetting();
		utensilSetting.setManyPeople();

		assertTrue(utensilSetting.isUtensilProhibited(ObligatoryUtensil.PAN));
		assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.POT));
		assertFalse(utensilSetting.isUtensilProhibited(ObligatoryUtensil.CASSEROLE));
	}
}