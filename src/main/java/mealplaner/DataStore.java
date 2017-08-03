package mealplaner;

import java.util.Date;

import mealplaner.model.MealListData;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public interface DataStore {
	void register(DataStoreListener listener);

	int getDaysPassed();

	Proposal getLastProposal();

	Date getTime();

	MealListData getMealListData();

	Settings[] getDefaultSettings();
}
