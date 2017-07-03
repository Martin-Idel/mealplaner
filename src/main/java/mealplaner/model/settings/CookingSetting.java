package mealplaner.model.settings;

import mealplaner.model.Meal;

public interface CookingSetting {
	boolean prohibits(Meal meal);
}
