package mealplaner.model;

import static mealplaner.model.MealplanerCalendar.getDaysBetween;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class MealplanerCalendarTest {

	private MealplanerCalendar sut;

	@Before
	public void setup() {
		sut = new MealplanerCalendar(Calendar.getInstance());
	}

	@Ignore
	@Test
	public void setDateTest() {
		sut.setDate(2015, 0, 1);

		// TODO: Fix me

		// assertEquals(Calendar.JANUARY,
		// sut.getCalendar().get(Calendar.MONTH));
		// assertEquals(1, sut.getCalendar().get(Calendar.DAY_OF_YEAR));
		// assertEquals(2015, sut.getCalendar().get(Calendar.YEAR));
	}

	@Test
	public void getDaysPassedToWithSomeDateInThePast() {
		sut.setDate(2017, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2015, 0, 1);

		int daysPassed = sut.getDaysPassedTo(compareToCalendar);

		assertEquals(0, daysPassed);
	}

	@Test
	public void getDaysPassedToSameYear() {
		sut.setDate(2016, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2016, 5, 7);

		int daysPassed = sut.getDaysPassedTo(compareToCalendar);

		assertEquals(30 + 29 + 31 + 30 + 31 + 7, daysPassed);
	}

	@Test
	public void getDaysPassedToSameYearNoLeapYear() {
		sut.setDate(2015, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2015, 5, 7);

		int daysPassed = sut.getDaysPassedTo(compareToCalendar);

		assertEquals(30 + 28 + 31 + 30 + 31 + 7, daysPassed);
	}

	@Test
	public void getDaysPassedToLessThanAYear() {
		sut.setDate(2015, 9, 5);
		Calendar compareToCalendar = getCustomCalendar(2016, 1, 7);

		int daysPassed = sut.getDaysPassedTo(compareToCalendar);

		assertEquals(26 + 30 + 31 + 31 + 7, daysPassed);
	}

	@Test
	public void getDaysPassedToMoreThanAYear() {
		sut.setDate(2014, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2016, 5, 7);

		int daysPassed = sut.getDaysPassedTo(compareToCalendar);

		assertEquals(364 + 365 + 31 + 29 + 31 + 30 + 31 + 7, daysPassed);
	}

	@Test
	public void correctNumberOfDaysForDaysAppart() {
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();
		oldCal.set(30, 10, 5, 20, 0, 0);
		newCal.set(30, 10, 7, 20, 0, 0);

		int daysPassed = getDaysBetween(oldCal, newCal);

		assertEquals(2, daysPassed);
	}

	@Test
	public void correctNumberOfDaysIfOldLiesInTheFutureAppart() {
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();
		oldCal.set(31, 10, 5, 20, 0, 0);
		newCal.set(30, 10, 7, 20, 0, 0);

		int daysPassed = getDaysBetween(oldCal, newCal);

		assertEquals(0, daysPassed);
	}

	@Test
	public void correctNumberOfDaysForMonthsAndYearsAppart() {
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();
		oldCal.set(30, 10, 5, 20, 0, 0);
		newCal.set(32, 11, 7, 20, 0, 0);

		int daysPassed = getDaysBetween(oldCal, newCal);

		assertEquals(763, daysPassed);
	}

	@Test
	public void correctNumberOfDaysForDaysAppartButNotMillis() {
		Calendar oldCal = Calendar.getInstance();
		Calendar newCal = Calendar.getInstance();
		oldCal.set(30, 10, 5, 20, 0, 0);
		newCal.set(30, 10, 6, 3, 0, 0);

		int daysPassed = getDaysBetween(oldCal, newCal);

		assertEquals(1, daysPassed);
	}

	private Calendar getCustomCalendar(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar;
	}
}
