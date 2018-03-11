package mealplaner.model;

import static java.time.LocalDate.now;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static mealplaner.commons.Pair.of;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import mealplaner.commons.Pair;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.settings.Settings;

public final class Proposal {
  private final List<Meal> mealList;
  private final List<Settings> settingsList;
  private final LocalDate date;
  private final boolean includeToday;

  private Proposal(List<Meal> mealList, List<Settings> settingsList, LocalDate date,
      boolean includeToday) {
    this.mealList = mealList;
    this.settingsList = settingsList;
    this.date = date;
    this.includeToday = includeToday;
  }

  public static Proposal createProposal() {
    return new Proposal(new ArrayList<>(), new ArrayList<>(), now(), false);
  }

  public static Proposal from(boolean includeToday, List<Meal> meals, List<Settings> settings) {
    if (meals.size() != settings.size()) {
      throw new MealException("List of Settings and Meals must have the same size");
    }
    return new Proposal(meals, settings, now(), includeToday);
  }

  public static Proposal from(boolean includeToday, List<Meal> meals, List<Settings> settings,
      LocalDate date) {
    return new Proposal(meals, settings, date, includeToday);
  }

  public List<Meal> getProposalList() {
    return unmodifiableList(mealList);
  }

  public List<Settings> getSettingsList() {
    return unmodifiableList(settingsList);
  }

  public List<Pair<Meal, Settings>> getMealsAndSettings() {
    return unmodifiableList(IntStream.range(0, mealList.size())
        .mapToObj(number -> of(mealList.get(number), settingsList.get(number)))
        .collect(toList()));
  }

  public int getSize() {
    return mealList.size();
  }

  public Meal getItem(int index) {
    return mealList.get(index);
  }

  public boolean isToday() {
    return includeToday;
  }

  public LocalDate getTime() {
    return date;
  }

  public LocalDate getDateOfFirstProposedItem() {
    return includeToday ? date : date.plusDays(1);
  }

  @Override
  public String toString() {
    return "Proposal [mealList=" + mealList
        + ", settings=" + settingsList
        + ", calendar=" + date
        + ", includeToday=" + includeToday + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((date == null) ? 0 : date.hashCode());
    result = prime * result + (includeToday ? 1231 : 1237);
    result = prime * result + ((mealList == null) ? 0 : mealList.hashCode());
    result = prime * result + ((settingsList == null) ? 0 : settingsList.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Proposal other = (Proposal) obj;
    return date.equals(other.date)
        && includeToday == other.includeToday
        && mealList.equals(other.mealList)
        && settingsList.equals(other.settingsList);
  }
}