package mealplaner;
/**
 * Martin Idel
 * Mealplaner: base class with data base, algorithms for sorting and proposing + IO dialogs.
 * Does not need any GUI, only Proposal, Meal, Setting + basic IO support.
 **/

import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.MealListData;
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public class MealplanerData implements DataStore {
	private MealListData mealListData;
	private Settings[] defaultSettings;
	private MealplanerCalendar cal;
	private Proposal proposal;

	private List<DataStoreListener> listeners = new ArrayList<>();

	public MealplanerData() {
		mealListData = new MealListData();
		cal = new MealplanerCalendar();
		defaultSettings = new Settings[7];
		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = new Settings();
		}
		proposal = new Proposal();
	}

	public MealplanerData(MealListData mealListData, MealplanerCalendar cal,
			Settings[] defaultSettings, Proposal proposal) throws MealException {
		this.mealListData = mealListData;
		this.cal = cal;
		setDefaultSettings(defaultSettings);
		this.proposal = proposal;
	}

	@Override
	public int getDaysPassed() {
		return cal.getDaysPassedTo(Calendar.getInstance());
	}

	@Override
	public Calendar getCalendar() {
		return cal.getCalendar();
	}

	@Override
	public Settings[] getDefaultSettings() {
		return defaultSettings;
	}

	@Override
	public Proposal getLastProposal() {
		return proposal;
	}

	@Override
	public MealListData getMealListData() {
		return mealListData;
	}

	public void addMeal(Meal meal) {
		mealListData.addMealAtSortedPosition(meal);
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void setDate(int day, int month, int year) {
		cal.setDate(day, month, year);
		listeners.forEach(listener -> listener
				.updateData(DATE_UPDATED));
	}

	public void setDefaultSettings(Settings[] defaultSettings) throws MealException {
		if (defaultSettings.length == 7) {
			this.defaultSettings = defaultSettings;
			listeners.forEach(listener -> listener.updateData(SETTINGS_CHANGED));
		} else {
			throw new MealException("Default Settings not of size 7");
		}
	}

	public void setLastProposal(Proposal proposal) {
		this.proposal = proposal;
		listeners.forEach(listener -> listener.updateData(PROPOSAL_ADDED));
	}

	public void setMealListData(MealListData mealListData) {
		this.mealListData = mealListData;
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void update(List<Meal> mealsCookedLast) throws MealException {
		int daysSinceLastUpdate = cal.updateCalendar();
		mealListData.updateDaysPassed(daysSinceLastUpdate);
		mealListData.getMealList().stream();
		for (int i = 0; i < mealsCookedLast.size(); i++) {
			int indexOfMeal = mealListData.getMealList().indexOf(mealsCookedLast.get(i));
			if (indexOfMeal >= 0) {
				mealListData.get(indexOfMeal).setDaysPassed(mealsCookedLast.size() - i - 1);
			}
		}
		listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	@Override
	public void register(DataStoreListener listener) {
		listeners.add(listener);
	}
}