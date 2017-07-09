package mealplaner.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingSetting;

// TODO: Implement sort functionality within streams.
public class MealListData implements List<Meal>, Serializable {
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

	@Override
	public Meal get(int index) {
		return mealList.get(index);
	}

	@Override
	public int size() {
		return mealList.size();
	}

	public boolean isNotEmpty() {
		return !mealList.isEmpty();
	}

	public void setMealList(List<Meal> mealList) {
		this.mealList = new ArrayList<Meal>(mealList);
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

	public void updateDaysPassed(NonnegativeInteger daysSinceLastUpdate) {
		mealList.forEach(meal -> meal.addDaysPassed(daysSinceLastUpdate.value));
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
		mealList.forEach(
				meal -> meal.setPriority(meal.getPriority() + randomIntGenerator.nextInt(7)));
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

	@Override
	public boolean add(Meal meal) {
		throw new UnsupportedOperationException(
				"Cannot add meal, use addMealAtSortedPosition(Meal meal) instead");
	}

	@Override
	public void add(int index, Meal meal) {
		throw new UnsupportedOperationException(
				"Cannot add meal, use addMealAtSortedPosition(Meal meal) instead");
	}

	@Override
	public boolean addAll(Collection<? extends Meal> meals) {
		throw new UnsupportedOperationException(
				"Cannot add meal, use addMealAtSortedPosition(Meal meal) instead");
	}

	@Override
	public boolean addAll(int fromIndex, Collection<? extends Meal> meals) {
		throw new UnsupportedOperationException(
				"Cannot add meal, use addMealAtSortedPosition(Meal meal) instead");
	}

	@Override
	public void clear() {
		mealList.clear();
	}

	@Override
	public boolean contains(Object element) {
		return mealList.contains(element);
	}

	@Override
	public boolean containsAll(Collection<?> elements) {
		return mealList.containsAll(elements);
	}

	@Override
	public int indexOf(Object element) {
		return mealList.indexOf(element);
	}

	@Override
	public boolean isEmpty() {
		return mealList.isEmpty();
	}

	@Override
	public Iterator<Meal> iterator() {
		return mealList.iterator();
	}

	@Override
	public int lastIndexOf(Object element) {
		return mealList.lastIndexOf(element);
	}

	@Override
	public ListIterator<Meal> listIterator() {
		return mealList.listIterator();
	}

	@Override
	public ListIterator<Meal> listIterator(int index) {
		return mealList.listIterator(index);
	}

	@Override
	public boolean remove(Object element) {
		return mealList.remove(element);
	}

	@Override
	public Meal remove(int index) {
		return mealList.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> elements) {
		return mealList.removeAll(elements);
	}

	@Override
	public boolean retainAll(Collection<?> elements) {
		return mealList.retainAll(elements);
	}

	@Override
	public Meal set(int index, Meal meal) {
		return mealList.set(index, meal);
	}

	@Override
	public List<Meal> subList(int fromIndex, int toIndex) {
		return mealList.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return mealList.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return mealList.toArray(a);
	}
}