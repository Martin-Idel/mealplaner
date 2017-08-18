package mealplaner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.model.Meal;
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.Settings;

@RunWith(MockitoJUnitRunner.class)
public class MealplanerDataTest {

	private List<Meal> meals;
	@Mock
	private Meal meal1;
	@Mock
	private Meal meal2;
	@Mock
	private Meal meal3;
	@Mock
	private Meal meal4;

	private MealplanerData sut;

	@Test
	public void addMealAtSortedPosition() {
		buildArrayListForComparison();
		sut = new MealplanerData(meals, new MealplanerCalendar(Calendar.getInstance()),
				new Settings[7], new Proposal());

		sut.addMeal(meal4);

		assertEquals(4, meals.size());
		assertEquals(meal4, meals.get(2));
	}

	@Ignore
	@Test
	public void updateMealCorrectlyAddsDaysToNonCookedMeals() {

	}

	@Ignore
	@Test
	public void updateMealCorrectlyUpdatesCookedMeals() {

	}

	private void buildArrayListForComparison() {
		meals = new ArrayList<>();
		when(meal4.compareTo(meal1)).thenReturn(1);
		meals.add(meal1);
		when(meal4.compareTo(meal2)).thenReturn(1);
		meals.add(meal2);
		when(meal4.compareTo(meal3)).thenReturn(-1);
		meals.add(meal3);
	}

	private void buildArrayListWithThreeItemsAndAllCookingPreferences() {
		meals = new ArrayList<>();
		when(meal1.getCookingPreference()).thenReturn(CookingPreference.NO_PREFERENCE);
		meals.add(meal1);
		when(meal2.getCookingPreference()).thenReturn(CookingPreference.RARE);
		meals.add(meal2);
		when(meal3.getCookingPreference()).thenReturn(CookingPreference.VERY_POPULAR);
		meals.add(meal3);
	}

	private void buildArrayListWithThreeItemsAndDifferentSideDishes() {
		meals = new ArrayList<>();
		when(meal1.getSidedish()).thenReturn(Sidedish.NONE);
		meals.add(meal1);
		when(meal2.getSidedish()).thenReturn(Sidedish.PASTA);
		meals.add(meal2);
		when(meal3.getSidedish()).thenReturn(Sidedish.POTATOES);
		meals.add(meal3);
	}
}
