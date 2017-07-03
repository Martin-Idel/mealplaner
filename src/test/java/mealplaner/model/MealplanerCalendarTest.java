package mealplaner.model;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class MealplanerCalendarTest {
	
	private MealplanerCalendar sut;

	@Before
	public void setup() {
		sut = new MealplanerCalendar();
	}

	@Test
	public void setDateTest() {
		sut.setDate(2015, 0, 1);

		assertEquals(Calendar.JANUARY, sut.getCalendar().get(Calendar.MONTH));
		assertEquals(1, sut.getCalendar().get(Calendar.DAY_OF_YEAR));
		assertEquals(2015, sut.getCalendar().get(Calendar.YEAR));
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
		
		assertEquals(30+29+31+30+31+7, daysPassed);
	}
	
	@Test
	public void getDaysPassedToSameYearNoLeapYear() {
		sut.setDate(2015, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2015, 5, 7);
		
		int daysPassed = sut.getDaysPassedTo(compareToCalendar);
		
		assertEquals(30+28+31+30+31+7, daysPassed);
	}
	
	@Test
	public void getDaysPassedToLessThanAYear() {
		sut.setDate(2015, 9, 5);
		Calendar compareToCalendar = getCustomCalendar(2016, 1, 7);
		
		int daysPassed = sut.getDaysPassedTo(compareToCalendar);
		
		assertEquals(26+30+31+31+7, daysPassed);
	}
	
	@Test
	public void getDaysPassedToMoreThanAYear() {
		sut.setDate(2014, 0, 1);
		Calendar compareToCalendar = getCustomCalendar(2016, 5, 7);
		
		int daysPassed = sut.getDaysPassedTo(compareToCalendar);
		
		assertEquals(364+365+31+29+31+30+31+7, daysPassed);
	}
	
	
	private Calendar getCustomCalendar(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		return calendar;
	}
}
