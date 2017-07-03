package mealplaner.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingSetting;

@RunWith(MockitoJUnitRunner.class)
public class MealListDataTest {

	private ArrayList<Meal> mealList;
	@Mock private Meal meal1;
	@Mock private Meal meal2;
	@Mock private Meal meal3;
	@Mock private Meal meal4;
	private MealListData sut;

	@Test
	public void addMealAtSortedPosition() {
		buildArrayListForComparison();
		sut = new MealListData(mealList);

		sut.addMealAtSortedPosition(meal4);

		assertEquals(4, mealList.size());
		assertEquals(meal4, mealList.get(2));
	}

	@Test
	public void updateDaysPassed() throws MealException {
		mealList = new ArrayList<>();
		mealList.add(meal1);
		mealList.add(meal2);
		sut = new MealListData(mealList);

		sut.updateDaysPassed(5);

		verify(meal1).addDaysPassed(5);
		verify(meal2).addDaysPassed(5);
	}

	@Test
	public void setPriorityToDaysPassedPlus() {
		mealList = new ArrayList<>();
		when(meal1.getDaysPassed()).thenReturn(10);
		when(meal2.getDaysPassed()).thenReturn(32);
		mealList.add(meal1);
		mealList.add(meal2);
		sut = new MealListData(mealList);

		sut.setPriorityToDaysPassedPlus(3);

		verify(meal1).setPriority(13);
		verify(meal2).setPriority(35);
	}

	@Test
	public void setPriorityOfMeal() {
		mealList = new ArrayList<>();
		when(meal1.getName()).thenReturn("Meal1");
		when(meal2.getName()).thenReturn("Meal2");
		mealList.add(meal1);
		mealList.add(meal2);
		sut = new MealListData(mealList);

		sut.setPriorityOfMeal(meal2, 3);

		verify(meal1, never()).setPriority(anyInt());
		verify(meal2).setPriority(3);
	}

	@Test
	public void setPreferenceMultipliersRare() {
		buildArrayListWithThreeItemsAndAllCookingPreferences();
		sut = new MealListData(mealList);

		sut.setPreferenceMutlipliers(CookingPreference.RARE, 2);

		verify(meal1, never()).multiplyPriority(anyFloat());
		verify(meal2).multiplyPriority(2);
		verify(meal3, never()).multiplyPriority(anyFloat());
	}

	@Test
	public void setSidedishMultipliers() {
		buildArrayListWithThreeItemsAndDifferentSideDishes();
		sut = new MealListData(mealList);

		sut.setSidedishMultipliers(Sidedish.POTATOES, 3);

		verify(meal1, never()).multiplyPriority(anyFloat());
		verify(meal2, never()).multiplyPriority(anyFloat());
		verify(meal3).multiplyPriority(3);
	}

	@Test
	public void prohibitMealWithSetting() {
		CookingSetting cookingSetting = mock(CookingSetting.class);
		when(cookingSetting.prohibits(meal1)).thenReturn(false);
		when(cookingSetting.prohibits(meal2)).thenReturn(true);
		when(cookingSetting.prohibits(meal3)).thenReturn(false);
		mealList = new ArrayList<>();
		mealList.add(meal1);
		mealList.add(meal2);
		mealList.add(meal3);
		sut = new MealListData(mealList);

		sut.prohibitMealWithSetting(cookingSetting);

		verify(meal1, never()).prohibit();
		verify(meal2).prohibit();
		verify(meal3, never()).prohibit();
	}

	private void buildArrayListForComparison() {
		mealList = new ArrayList<>();
		when(meal4.compareTo(meal1)).thenReturn(1);
		mealList.add(meal1);
		when(meal4.compareTo(meal2)).thenReturn(1);
		mealList.add(meal2);
		when(meal4.compareTo(meal3)).thenReturn(-1);
		mealList.add(meal3);
	}

	private void buildArrayListWithThreeItemsAndAllCookingPreferences() {
		mealList = new ArrayList<>();
		when(meal1.getCookingPreference()).thenReturn(CookingPreference.NO_PREFERENCE);
		mealList.add(meal1);
		when(meal2.getCookingPreference()).thenReturn(CookingPreference.RARE);
		mealList.add(meal2);
		when(meal3.getCookingPreference()).thenReturn(CookingPreference.VERY_POPULAR);
		mealList.add(meal3);
	}

	private void buildArrayListWithThreeItemsAndDifferentSideDishes() {
		mealList = new ArrayList<>();
		when(meal1.getSidedish()).thenReturn(Sidedish.NONE);
		mealList.add(meal1);
		when(meal2.getSidedish()).thenReturn(Sidedish.PASTA);
		mealList.add(meal2);
		when(meal3.getSidedish()).thenReturn(Sidedish.POTATOES);
		mealList.add(meal3);
	}
}
