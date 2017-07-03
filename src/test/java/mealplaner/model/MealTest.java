package mealplaner.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class MealTest {
	
	private Meal sut;

	@Before
	public void setup() throws MealException{
		sut = new Meal("Test", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 5); 		
	}

	@Test
	public void setNameWithCorrectName() throws MealException {
		
		sut.setName("New Name");
		
		assertEquals("New Name", sut.getName());
	}
	
	@Test(expected=MealException.class)
	public void setNameWithOnlyWhitespace() throws MealException {
		
		sut.setName("  ");
		
		assertEquals("Test", sut.getName());
	}
	
	@Test
	public void setDaysPassedWithPositiveNumber() throws MealException {
		
		sut.setDaysPassed(154);
		
		assertEquals(154, sut.getDaysPassed());
	}
	
	@Test(expected=MealException.class)
	public void setDaysPassedWithNegativeNumber() throws MealException {
		
		sut.setDaysPassed(-5);
		
		assertEquals(5, sut.getDaysPassed());
	}
	
	@Test
	public void compareToWithPriority() throws MealException {
		Meal compareMeal = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1);
		sut.setCompareToPriority(true);
		compareMeal.setCompareToPriority(true);
		
		int compareResult = sut.compareTo(compareMeal);
		
		assertEquals(1,compareResult);
	}
	
	@Test
	public void compareToWithName() throws MealException {
		Meal compareMeal = new Meal("Test2", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1);
		sut.setCompareToPriority(false);
		compareMeal.setCompareToPriority(false);
		
		int compareResult = sut.compareTo(compareMeal);
		
		assertEquals(-1,compareResult);
	}
}