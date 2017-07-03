package mealplaner;

import java.util.Calendar;

import mealplaner.model.MealListData;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public interface DataStore {
	void register(DataStoreListener listener);

	int getDaysPassed();

	Proposal getLastProposal();

	Calendar getCalendar();

	MealListData getMealListData();

	Settings[] getDefaultSettings();
}
