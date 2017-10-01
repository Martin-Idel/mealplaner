package mealplaner.model;

import static mealplaner.io.XMLHelpers.getCalendarFromXml;
import static mealplaner.io.XMLHelpers.saveCalendarToXml;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MealplanerCalendar implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final int MILLISECONDS_PER_DAY = 86400000;
	private static final int LAST_HOUR = 23;
	private static final int LAST_MINUTE_OR_SECOND = 59;

	private Calendar date;

	public MealplanerCalendar(Calendar calendar) {
		date = calendar;
	}

	public int getToday() {
		return date.get(Calendar.DAY_OF_WEEK);
	}

	public Date getTime() {
		return date.getTime();
	}

	public void setDate(int year, int month, int day) {
		date.set(year, month, day);
	}

	public int updateCalendar() {
		Calendar newCal = Calendar.getInstance();
		int daysSince = getDaysPassedTo(newCal);
		date = newCal;
		return daysSince;
	}

	public int getDaysPassedTo(Calendar newCal) {
		return getDaysBetween(date, newCal);
	}

	public static int getDaysBetween(Calendar oldCal, Calendar newCal) {
		long daysSince = 0;
		if (newCal.after(oldCal)) {
			Calendar newCalWithDateAtTheEndOfDay = Calendar.getInstance();
			newCalWithDateAtTheEndOfDay.set(newCal.get(Calendar.YEAR),
					newCal.get(Calendar.MONTH),
					newCal.get(Calendar.DAY_OF_MONTH),
					LAST_HOUR, LAST_MINUTE_OR_SECOND, LAST_MINUTE_OR_SECOND);
			daysSince = (newCalWithDateAtTheEndOfDay.getTimeInMillis() - oldCal.getTimeInMillis())
					/ MILLISECONDS_PER_DAY;
		}
		return (int) daysSince;
	}

	public static Element saveToXml(Document doc, MealplanerCalendar calendar) {
		return saveCalendarToXml(doc, calendar.date);
	}

	public static MealplanerCalendar getMealplanerCalendarFromXml(Element calendarNode) {
		return new MealplanerCalendar(getCalendarFromXml(calendarNode));
	}
}
