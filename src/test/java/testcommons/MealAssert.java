package testcommons;

import org.assertj.core.api.AbstractAssert;

import mealplaner.model.Meal;

public class MealAssert extends AbstractAssert<MealAssert, Meal> {

	public MealAssert(Meal actual) {
		super(actual, MealAssert.class);
	}

	public static MealAssert assertThat(Meal actual) {
		return new MealAssert(actual);
	}

	public MealAssert isEqualTo(Meal meal) {
		if (!actual.equals(meal)) {
			failWithMessage("Expected meal to be %s but instead was %s",
					mealToString(meal),
					mealToString(actual));
		}
		return this;
	}

	private String mealToString(Meal meal) {
		return "Meal [name=" + meal.getName()
				+ ", cookingTime=" + meal.getCookingTime()
				+ ", cookingPreference=" + meal.getCookingPreference()
				+ ", cookingUtensil=" + meal.getObligatoryUtensil()
				+ ", sidedish=" + meal.getSidedish()
				+ ", daysPassed=" + meal.getDaysPassed()
				+ ", comment=" + meal.getComment() + "]";
	}
}
