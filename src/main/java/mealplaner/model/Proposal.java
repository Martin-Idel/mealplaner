package mealplaner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Proposal implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Meal> mealList;
	private Calendar calendar;
	private boolean includeToday;

	public Proposal() {
		this(new ArrayList<Meal>(), Calendar.getInstance(), false);
	}

	public Proposal(Calendar calendar, boolean includeToday) {
		this(new ArrayList<Meal>(), calendar, includeToday);
	}

	public Proposal(List<Meal> mealList, Calendar calendar, boolean includeToday) {
		this.mealList = mealList;
		this.calendar = calendar;
		this.includeToday = includeToday;
	}

	public List<Meal> getProposalList() {
		return mealList;
	}

	public void setProposalList(ArrayList<Meal> proposal) {
		mealList = proposal;
	}

	public void clearProposalList() {
		mealList.clear();
	}

	public void addItemToProposalList(Meal meal) {
		mealList.add(meal);
	}

	public int getSize() {
		return mealList.size();
	}

	public Meal getItem(int index) {
		return mealList.get(index);
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calender) {
		this.calendar = calender;
	}

	public boolean isToday() {
		return includeToday;
	}

	public void setToday(boolean today) {
		this.includeToday = today;
	}

	public Date getTime() {
		return calendar.getTime();
	}

	public void setDate(Date time) {
		calendar.setTime(time);
	}
}