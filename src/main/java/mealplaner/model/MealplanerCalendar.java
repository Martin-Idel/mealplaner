package mealplaner.model;

import java.io.Serializable;
import java.util.Calendar;

public class MealplanerCalendar implements Serializable {

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
		date.set(year,month,day);
	}	
	
	public int updateCalendar() {
		Calendar newCal = Calendar.getInstance();
		int daysSince = getDaysPassedTo(newCal);
		setCalendar(newCal);
		return daysSince;
	}
	
	public int getDaysPassedTo(Calendar newCal){
		int dayOld = date.get(Calendar.DAY_OF_YEAR);	
		int yearOld = date.get(Calendar.YEAR);
		int dayNow = newCal.get(Calendar.DAY_OF_YEAR);	
		int yearNow = newCal.get(Calendar.YEAR);
		
		int daysBetweenOldAndNow = 0;
		if(newDateLiesInTheFutureOfOldDate(dayOld, yearOld, dayNow, yearNow)){
			daysBetweenOldAndNow += addCompleteYears(yearOld, yearNow);
			daysBetweenOldAndNow += addOrSubstractFurtherDays(dayOld, dayNow); 
		}
		return daysBetweenOldAndNow;
	}

	private boolean newDateLiesInTheFutureOfOldDate(int dayOld, int yearOld, int dayNow, int yearNow) {
		return dayNow>=dayOld && yearNow>=yearOld || dayNow<dayOld && yearNow>yearOld;
	}

	private int addCompleteYears(int yearOld, int yearNow) {
		int daysInBetweenYearOldAndYearNow = 0;
		for(int i=yearOld; i<yearNow; i++){
			daysInBetweenYearOldAndYearNow += (i%4)==0 ? 366 : 365;
		}
		return daysInBetweenYearOldAndYearNow;
	}
	
	private int addOrSubstractFurtherDays(int dayOld, int dayNow) {
		return dayNow-dayOld;
	}
}
