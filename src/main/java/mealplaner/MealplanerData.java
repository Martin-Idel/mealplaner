package mealplaner;

import static java.lang.Math.toIntExact;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.stream.Collectors.toList;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.MealBuilder.from;
import static mealplaner.model.Proposal.createProposal;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;

public class MealplanerData implements DataStore {
  private List<Meal> meals;
  private DefaultSettings defaultSettings;
  private LocalDate date;
  private Proposal proposal;

  private final List<DataStoreListener> listeners = new ArrayList<>();

  public MealplanerData() {
    meals = new ArrayList<Meal>();
    date = now();
    defaultSettings = createDefaultSettings();
    proposal = createProposal();
  }

  public MealplanerData(
      List<Meal> meals,
      LocalDate date,
      DefaultSettings defaultSettings,
      Proposal proposal) {
    this.meals = meals;
    this.date = date;
    this.defaultSettings = defaultSettings;
    this.proposal = proposal;
  }

  @Override
  public int getDaysPassed() {
    return toIntExact(DAYS.between(date, now()));
  }

  @Override
  public LocalDate getTime() {
    return date;
  }

  @Override
  public DefaultSettings getDefaultSettings() {
    return defaultSettings;
  }

  @Override
  public Proposal getLastProposal() {
    return proposal;
  }

  @Override
  public List<Meal> getMeals() {
    return meals;
  }

  public void setMeals(List<Meal> meals) {
    this.meals = new ArrayList<>(meals); // defensive copy
    listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
  }

  public void addMeal(Meal neu) {
    meals.add(neu);
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
    listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
  }

  public void setDefaultSettings(DefaultSettings defaultSettings) {
    this.defaultSettings = defaultSettings;
    listeners.forEach(listener -> listener.updateData(SETTINGS_CHANGED));
  }

  public void setLastProposal(Proposal proposal) {
    this.proposal = proposal;
    listeners.forEach(listener -> listener.updateData(PROPOSAL_ADDED));
  }

  public void update(List<Meal> mealsCookedLast, LocalDate now) {
    NonnegativeInteger daysSinceLastUpdate = nonNegative(toIntExact(DAYS.between(date, now)));
    date = now;
    meals = meals.stream().map(meal -> mealsCookedLast.contains(meal)
        ? from(meal)
            .daysPassed(nonNegative(
                mealsCookedLast.size() - mealsCookedLast.indexOf(meal) - 1))
            .create()
        : from(meal).addDaysPassed(daysSinceLastUpdate).create())
        .collect(toList());
    listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
    listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
  }

  @Override
  public void register(DataStoreListener listener) {
    listeners.add(listener);
  }
}