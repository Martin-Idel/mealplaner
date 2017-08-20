package mealplaner.model;

import java.io.Serializable;

import mealplaner.errorhandling.ErrorKeys;
import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

public class Meal implements Serializable, Comparable<Meal>, ErrorKeys {
	private static final long serialVersionUID = 1L;
	private String name;
	private CookingTime cookingTime;
	private Sidedish sidedish;
	private ObligatoryUtensil obligatoryUtensil;
	private CookingPreference cookingPreference;
	private int daysPassed;
	private String comment;

	public Meal(String name, CookingTime cookingTime, Sidedish sideDish,
			ObligatoryUtensil obligatoryUtensil,
			CookingPreference cookingPreference, int daysPassed, String comment)
			throws MealException {
		setName(name);
		this.cookingTime = cookingTime;
		this.sidedish = sideDish;
		this.obligatoryUtensil = obligatoryUtensil;
		this.cookingPreference = cookingPreference;
		setDaysPassed(daysPassed);
		this.setComment(comment);
	}

	public Meal(Meal meal) {
		setName(meal.getName());
		this.cookingTime = meal.getCookingTime();
		this.sidedish = meal.getSidedish();
		this.obligatoryUtensil = meal.getObligatoryUtensil();
		this.cookingPreference = meal.getCookingPreference();
		setDaysPassed(meal.getDaysPassed());
		this.setComment(meal.getComment());
	}

	@Override
	public int compareTo(Meal otherMeal) {
		return this.getName().compareToIgnoreCase(otherMeal.getName());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws MealException {
		if (name.trim().isEmpty()) {
			throw new MealException("Name is empty or consists only of whitespace");
		} else {
			this.name = name.trim();
		}
	}

	public int getDaysPassed() {
		return daysPassed;
	}

	public void setDaysPassed(int daysPassed) throws MealException {
		if (daysPassed >= 0) {
			this.daysPassed = daysPassed;
		} else {
			throw new MealException("Number must be nonnegative");
		}
	}

	public void addDaysPassed(int daysPassedSince) throws MealException {
		setDaysPassed(daysPassed + daysPassedSince);
	}

	public CookingTime getCookingTime() {
		return cookingTime;
	}

	public void setCookingTime(CookingTime cookingTime) {
		this.cookingTime = cookingTime;
	}

	public Sidedish getSidedish() {
		return sidedish;
	}

	public void setSidedish(Sidedish sidedish) {
		this.sidedish = sidedish;
	}

	public ObligatoryUtensil getObligatoryUtensil() {
		return obligatoryUtensil;
	}

	public void setObligatoryUtensil(ObligatoryUtensil obligatoryUtensil) {
		this.obligatoryUtensil = obligatoryUtensil;
	}

	public CookingPreference getCookingPreference() {
		return cookingPreference;
	}

	public void setCookingPreference(CookingPreference cookingPreference) {
		this.cookingPreference = cookingPreference;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return name;
	}
}