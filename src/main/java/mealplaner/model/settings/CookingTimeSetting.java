package mealplaner.model.settings;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import mealplaner.model.Meal;
import mealplaner.model.enums.CookingTime;

public class CookingTimeSetting implements Serializable, CookingSetting {

	private static final long serialVersionUID = 1L;
	private final Set<CookingTime> prohibitedCookingTime;

	public CookingTimeSetting() {
		this(new HashSet<CookingTime>());
	}

	public CookingTimeSetting(CookingTime... cookingTime) {
		this(new HashSet<CookingTime>());
		prohibitedCookingTime.addAll(Arrays.asList(cookingTime));
	}

	public CookingTimeSetting(CookingTimeSetting cookingTimeSetting) {
		this(new HashSet<CookingTime>(cookingTimeSetting.prohibitedCookingTime));
	}

	public CookingTimeSetting(Set<CookingTime> prohibitedUtensil) {
		this.prohibitedCookingTime = prohibitedUtensil;
	}

	public void prohibitCookingTime(CookingTime cookingTime) {
		prohibitedCookingTime.add(cookingTime);
	}

	public void allowCookingTime(CookingTime cookingTime) {
		prohibitedCookingTime.remove(cookingTime);
	}

	public void reset() {
		prohibitedCookingTime.removeAll(Arrays.asList(CookingTime.values()));
	}

	public boolean isTimeProhibited(CookingTime cookingTime) {
		return prohibitedCookingTime.contains(cookingTime);
	}

	@Override
	public boolean prohibits(Meal meal) {
		return prohibitedCookingTime.contains(meal.getCookingTime());
	}

	public boolean contains(CookingTime cookingTime) {
		return prohibitedCookingTime.contains(cookingTime);
	}
}
