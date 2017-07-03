package mealplaner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import mealplaner.errorhandling.MealException;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingSetting;

// TODO: Implement sort functionality within streams.
public class MealListData implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Meal> mealList;

	public MealListData() {
		mealList = new ArrayList<Meal>();
	}

	public MealListData(ArrayList<Meal> mealList) {
		this.mealList = mealList;
	}

	public MealListData(MealListData mealListData) {
		mealList = new ArrayList<Meal>();
		mealListData.mealList.stream().forEach(meal -> this.mealList.add(new Meal(meal)));
	}

	public Meal getMeal(int index) {
		return mealList.get(index);
	}

	public int getSize() {
		return mealList.size();
	}

	public boolean isNotEmpty() {
		return !mealList.isEmpty();
	}

	public void setMealList(ArrayList<Meal> mealList) {
		this.mealList = mealList;
		sort();
	}

	// TODO: should pass unmodifiable list
	public ArrayList<Meal> getMealList() {
		return mealList;
	}

	public String[] getMealsAsNames() {
		return mealList.stream()
				.map(meal -> meal.getName())
				.toArray(String[]::new);
	}

	public int addMealAtSortedPosition(Meal neu) {
		int index = 0;
		while (index < mealList.size() && neu.compareTo(mealList.get(index)) >= 0) {
			index++;
		}
		mealList.add(index, neu);
		return index;
	}

	public Meal getMealWithHighestPriority() {
		sortMealsAccordingToPriorities();
		return mealList.get(mealList.size() - 1);
	}

	public Optional<List<Meal>> getLastCookedDishes(int numberOfDishes) {
		sortMealsAccordingToDaysPassed();
		if (numberOfDishes <= mealList.size()) {
			return Optional.ofNullable(mealList.subList(0, numberOfDishes));
		}
		return Optional.empty();
	}

	public void removeMeal(Meal meal) {
		mealList.remove(meal);
	}

	// TODO: This shall be a stream. Maybe even define type positiveINT.
	public void updateDaysPassed(int daysSinceLastUpdate) throws MealException {
		for (Meal meal : mealList) {
			meal.addDaysPassed(daysSinceLastUpdate);
		}
	}

	public void setPriorityToDaysPassedPlus(int daysSinceStartOfUpdate) {
		mealList.forEach(meal -> meal.setPriority(meal.getDaysPassed() + daysSinceStartOfUpdate));
	}

	public void setPriorityOfMeal(Meal meal, int priority) {
		mealList.stream()
				.filter(mealInList -> mealInList.getName().equals(meal.getName()))
				.forEach(mealInList -> mealInList.setPriority(priority));
	}

	private void sortMealsAccordingToDaysPassed() {
		mealList.forEach(meal -> {
			meal.setPriority(meal.getDaysPassed());
			meal.setCompareToPriority(true);
		});
		sort();
	}

	public void sortMealsAccordingToPriorities() {
		mealList.forEach(meal -> meal.setCompareToPriority(true));
		sort();
	}

	public void sortMealsAccordingToName() {
		mealList.stream().forEach(meal -> {
			meal.setPriority(meal.getDaysPassed());
			meal.setCompareToPriority(false);
		});
		sort();
	}

	private void sort() {
		Collections.sort(mealList);
	}

	public void addRandomIntegersToPriorities() {
		Random randomIntGenerator = new Random();
		mealList.forEach(meal -> meal.setPriority(meal.getPriority() + randomIntGenerator.nextInt(7)));
	}

	public void setPreferenceMutlipliers(CookingPreference compareToPreference, float multiplier) {
		mealList.stream()
				.filter(meal -> meal.getCookingPreference().equals(compareToPreference))
				.forEach(meal -> meal.multiplyPriority(multiplier));
	}

	public void setSidedishMultipliers(Sidedish compareSidedish, float multiplier) {
		mealList.stream()
				.filter(meal -> meal.getSidedish().equals(compareSidedish))
				.forEach(meal -> meal.multiplyPriority(multiplier));
	}

	public void prohibitMealWithSetting(CookingSetting cookingSetting) {
		mealList.stream()
				.filter(meal -> cookingSetting.prohibits(meal))
				.forEach(meal -> meal.prohibit());
	}
}