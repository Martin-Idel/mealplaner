package mealplaner.model;

import static java.time.LocalDate.now;
import static java.util.Collections.unmodifiableList;
import static mealplaner.model.ProposedMenu.mainOnly;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.settings.Settings;

// TODO: Fix interface
public final class Proposal {
  private final List<ProposedMenu> mealList;
  private final LocalDate date;
  private final boolean includeToday;

  private Proposal(List<Meal> mealList, List<Settings> settingsList, LocalDate date,
      boolean includeToday) {
    this.mealList = createProposedMenuListFrom(mealList, settingsList);
    this.date = date;
    this.includeToday = includeToday;
  }

  private Proposal(List<ProposedMenu> proposedMenu, LocalDate date, boolean includeToday) {
    this.mealList = proposedMenu;
    this.date = date;
    this.includeToday = includeToday;
  }

  private List<ProposedMenu> createProposedMenuListFrom(List<Meal> meals, List<Settings> settings) {
    if (meals.size() != settings.size()) {
      throw new MealException("Number of proposed meals and settings does not fit");
    }
    List<ProposedMenu> proposedMenu = new ArrayList<>();
    for (int i = 0; i < meals.size(); i++) {
      proposedMenu.add(createProposedMenuFrom(meals.get(i), settings.get(i)));
    }
    return proposedMenu;
  }

  private ProposedMenu createProposedMenuFrom(Meal meal, Settings settings) {
    return mainOnly(meal.getId(), settings.getNumberOfPeople());
  }

  public static Proposal createProposal() {
    return new Proposal(new ArrayList<>(), new ArrayList<>(), now(), false);
  }

  public static Proposal from(boolean includeToday, List<ProposedMenu> proposedMenu) {
    return new Proposal(proposedMenu, now(), includeToday);
  }

  public static Proposal from(boolean includeToday, List<ProposedMenu> proposedMenu,
      LocalDate date) {
    return new Proposal(proposedMenu, date, includeToday);
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

  public List<ProposedMenu> getProposalList() {
    return unmodifiableList(mealList);
  }

  public int getSize() {
    return mealList.size();
  }

  public ProposedMenu getItem(int index) {
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
        + ", calendar=" + date
        + ", includeToday=" + includeToday + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + date.hashCode();
    result = prime * result + (includeToday ? 1231 : 1237);
    result = prime * result + mealList.hashCode();
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
        && mealList.equals(other.mealList);
  }
}