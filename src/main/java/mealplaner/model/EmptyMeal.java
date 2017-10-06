package mealplaner.model;

import static java.util.Optional.empty;

import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public final class EmptyMeal extends Meal {
	public EmptyMeal() {
		super("EMPTY", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.CASSEROLE,
				CookingPreference.RARE, 0, "", empty());
	}

	@Override
	public String toString() {
		return "";
	}
}
