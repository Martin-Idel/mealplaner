package mealplaner;

import java.util.Calendar;

import mealplaner.model.Proposal;

// TODO: Not used yet
// TODO: Use in MealplanerCalendar
// TODO: Maybe use millisecond compare function as it is shorter.
public class Util {
	private static final int MILLISECONDS_PER_DAY = 86400000;

	public static int getDaysPassedSinceTo(Calendar oldCal, Calendar newCal) {
		int dayOld = oldCal.get(Calendar.DAY_OF_YEAR);
		int yearOld = oldCal.get(Calendar.YEAR);
		int dayNow = newCal.get(Calendar.DAY_OF_YEAR);
		int yearNow = newCal.get(Calendar.YEAR);

		int daysBetweenOldAndNow = 0;
		if (newDateLiesInTheFutureOfOldDate(dayOld, yearOld, dayNow, yearNow)) {
			daysBetweenOldAndNow += addCompleteYears(yearOld, yearNow);
			daysBetweenOldAndNow += addOrSubstractFurtherDays(dayOld, dayNow);
		}
		return daysBetweenOldAndNow;
	}

	private static boolean newDateLiesInTheFutureOfOldDate(int dayOld, int yearOld, int dayNow, int yearNow) {
		return dayNow >= dayOld && yearNow >= yearOld || dayNow < dayOld && yearNow > yearOld;
	}

	private static int addCompleteYears(int yearOld, int yearNow) {
		int daysInBetweenYearOldAndYearNow = 0;
		for (int i = yearOld; i < yearNow; i++) {
			daysInBetweenYearOldAndYearNow += (i % 4) == 0 ? 366 : 365;
		}
		return daysInBetweenYearOldAndYearNow;
	}

	private static int addOrSubstractFurtherDays(int dayOld, int dayNow) {
		return dayNow - dayOld;
	}

	// TODO: This needs testing. It also seems wrong if we don't do everything
	// later in the day...
	public int daysPassedSinceLastProposal(Proposal proposal) {
		Calendar newCal = Calendar.getInstance();
		Calendar oldCal = proposal.getCalendar();
		long daysSince = 0;
		if (newCal.after(oldCal.getTime())) {
			daysSince = (newCal.getTimeInMillis() - oldCal.getTimeInMillis()) / MILLISECONDS_PER_DAY;
		}
		return (int) daysSince;
	}
}
