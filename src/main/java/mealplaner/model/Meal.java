package mealplaner.model;
/**
 * Martin Idel
 * Meal: base class, representing a menu.
 * Serializable and Comparable (with other meals, comparison by names in alphabetical order).
 **/

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
	private int priority;
	private boolean compareToPriority = false;

	public Meal(String name, CookingTime cookingTime, Sidedish sideDish, ObligatoryUtensil obligatoryUtensil,
			CookingPreference cookingPreference, int daysPassed) throws MealException {
		setName(name);
		this.cookingTime = cookingTime;
		this.sidedish = sideDish;
		this.obligatoryUtensil = obligatoryUtensil;
		this.cookingPreference = cookingPreference;
		setDaysPassed(daysPassed);
		setPriority(daysPassed);
	}

	public Meal(Meal meal) {
		setName(meal.getName());
		this.cookingTime = meal.getCookingTime();
		this.sidedish = meal.getSidedish();
		this.obligatoryUtensil = meal.getObligatoryUtensil();
		this.cookingPreference = meal.getCookingPreference();
		setDaysPassed(meal.getDaysPassed());
		setPriority(meal.getPriority());
	}

	@Override
	public int compareTo(Meal otherMeal) {
		return compareToPriority
				? ((Integer) this.getPriority()).compareTo((Integer) otherMeal.getPriority())
				: this.getName().compareToIgnoreCase(otherMeal.getName());
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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void prohibit() {
		this.priority = -1;
	}

	public void multiplyPriority(float factor) {
		this.priority = (int) (factor * this.priority);
	}

	public boolean doCompareToPriority() {
		return compareToPriority;
	}

	public void setCompareToPriority(boolean c) {
		compareToPriority = c;
	}

	@Override
	public String toString() {
		return name;
	}
}