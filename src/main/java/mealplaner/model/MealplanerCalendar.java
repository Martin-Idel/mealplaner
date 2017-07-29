package mealplaner.model;

import java.io.Serializable;
import java.util.Calendar;

public class MealplanerCalendar implements Serializable {
	private static final int MILLISECONDS_PER_DAY = 86400000;
	private static final int LAST_HOUR = 23;
	private static final int LAST_MINUTE_OR_SECOND = 59;

	private static final long serialVersionUID = 1L;

	private Calendar date;

	public MealplanerCalendar() {
		date = Calendar.getInstance();
	}

	public Calendar getCalendar() {
		return date;
	}

	public void setCalendar(Calendar calendar) {
		date = calendar;
	}

	public void setDate(int year, int month, int day) {
		date.set(year, month, day);
	}

	public int updateCalendar() {
		Calendar newCal = Calendar.getInstance();
		int daysSince = getDaysPassedTo(newCal);
		setCalendar(newCal);
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
}
