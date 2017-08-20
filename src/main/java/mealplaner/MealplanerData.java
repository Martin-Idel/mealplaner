package mealplaner;

import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public class MealplanerData implements DataStore {
	private List<Meal> meals;
	private Settings[] defaultSettings;
	private MealplanerCalendar cal;
	private Proposal proposal;

	private List<DataStoreListener> listeners = new ArrayList<>();

	public MealplanerData() {
		meals = new ArrayList<Meal>();
		cal = new MealplanerCalendar(Calendar.getInstance());
		defaultSettings = new Settings[7];
		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = new Settings();
		}
		proposal = new Proposal();
	}

	public MealplanerData(List<Meal> meals, MealplanerCalendar cal,
			Settings[] defaultSettings, Proposal proposal) throws MealException {
		this.meals = meals;
		this.cal = cal;
		setDefaultSettings(defaultSettings);
		this.proposal = proposal;
	}

	@Override
	public int getDaysPassed() {
		return cal.getDaysPassedTo(Calendar.getInstance());
	}

	@Override
	public Date getTime() {
		return cal.getTime();
	}

	public int getToday() {
		return cal.getToday();
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
	public List<Meal> getMeals() {
		return meals;
	}

	public void setMeals(List<Meal> meals) {
		this.meals = meals;
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void addMeal(Meal neu) {
		meals.add(neu);
		meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	public void setDate(int day, int month, int year) {
		cal.setDate(day, month, year);
		listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
	}

	public void setDefaultSettings(Settings[] defaultSettings) throws MealException {
		if (defaultSettings.length == 7) {
			for (Settings setting : defaultSettings) {
				if (setting == null) {
					throw new MealException("One of the default settings is null");
				}
			}
			this.defaultSettings = defaultSettings;
			listeners.forEach(listener -> listener.updateData(SETTINGS_CHANGED));
		} else {
			throw new MealException("Default settings not of size 7");
		}
	}

	public void setLastProposal(Proposal proposal) {
		this.proposal = proposal;
		listeners.forEach(listener -> listener.updateData(PROPOSAL_ADDED));
	}

	public void update(List<Meal> mealsCookedLast) {
		int daysSinceLastUpdate = cal.updateCalendar();
		meals.forEach(meal -> meal.setDaysPassed(mealsCookedLast.contains(meal)
				? mealsCookedLast.size() - mealsCookedLast.indexOf(meal) - 1
				: meal.getDaysPassed() + daysSinceLastUpdate));
		listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
		listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
	}

	@Override
	public void register(DataStoreListener listener) {
		listeners.add(listener);
	}
}