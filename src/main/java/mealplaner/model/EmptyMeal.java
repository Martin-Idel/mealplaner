package mealplaner.model;

import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public final class EmptyMeal extends Meal {
	private static final long serialVersionUID = 1L;

	public EmptyMeal() {
		super("EMPTY", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, 0);
	}

	@Override
	public String toString() {
		return "";
	}
}
