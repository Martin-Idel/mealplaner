package mealplaner;

import java.util.Date;
import java.util.List;

import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;

public interface DataStore {
	void register(DataStoreListener listener);

	int getDaysPassed();

	Proposal getLastProposal();

	Date getTime();

	List<Meal> getMeals();

	Settings[] getDefaultSettings();
}
