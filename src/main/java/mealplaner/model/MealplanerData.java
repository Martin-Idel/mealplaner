package mealplaner.model;

import static java.lang.Math.toIntExact;
import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.model.DataStoreEventType.DATE_UPDATED;
import static mealplaner.model.DataStoreEventType.INGREDIENTS_CHANGED;
import static mealplaner.model.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.model.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.model.proposal.Proposal.createProposal;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.settings.DefaultSettings;

public final class MealplanerData implements DataStore {
  private List<Ingredient> ingredients;
  private final MealData mealData;
  private DefaultSettings defaultSettings;
  private LocalDate date;
  private Proposal proposal;

  private final List<DataStoreListener> listeners = new ArrayList<>();

  private static class MealplanerDataHolder {
    private static final MealplanerData INSTANCE = new MealplanerData(); // NOPMD
  }

  public static MealplanerData getInstance() {
    return MealplanerDataHolder.INSTANCE;
  }

  private MealplanerData() {
    ingredients = new ArrayList<Ingredient>();
    date = now();
    defaultSettings = createDefaultSettings();
    proposal = createProposal();
    mealData = MealData.createData(this);
  }

  public void clear() {
    ingredients = new ArrayList<Ingredient>();
    mealData.clear();
    date = now();
    defaultSettings = createDefaultSettings();
    proposal = createProposal();
  }

  @Override
  public int getDaysPassed() {
    return toIntExact(DAYS.between(date, now()));
  }

  @Override
  public LocalDate getTime() {
    return date;
  }

  public void setTime(LocalDate time) {
    this.date = time;
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
  public List<Ingredient> getIngredients() {
    ingredients.sort((ingredient1, ingredient2) -> ingredient1.getName()
        .compareTo(ingredient2.getName()));
    return new ArrayList<>(ingredients); // defensive copy
  }

  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
    listeners.forEach(listener -> listener.updateData(INGREDIENTS_CHANGED));
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = new ArrayList<>(ingredients); // defensive copy
    listeners.forEach(listener -> listener.updateData(INGREDIENTS_CHANGED));
  }

  @Override
  public List<Meal> getMeals() {
    return mealData.getMealsInList();
  }

  @Override
  public Optional<Meal> getMeal(UUID uuid) {
    return mealData.getMeal(uuid);
  }

  public void setMeals(List<Meal> meals) {
    mealData.setMeals(meals);
    listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
  }

  public void addMeal(Meal newMeal) {
    mealData.addMeal(newMeal);
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

  public void setProposalSummary(DefaultSettings defaultSettings,
      Proposal lastProposal,
      LocalDate time) {
    setDefaultSettings(defaultSettings);
    setLastProposal(lastProposal);
    setTime(time);
  }

  public void update(List<ProposedMenu> mealsCookedLast, LocalDate now) {
    NonnegativeInteger daysSinceLastUpdate = nonNegative(toIntExact(DAYS.between(date, now)));
    date = now;
    mealData.updateMeals(mealsCookedLast, daysSinceLastUpdate);
    listeners.forEach(listener -> listener.updateData(DATE_UPDATED));
    listeners.forEach(listener -> listener.updateData(DATABASE_EDITED));
  }

  @Override
  public void register(DataStoreListener listener) {
    listeners.add(listener);
  }

  @Override
  public void deregister(DataStoreListener listener) {
    listeners.remove(listener);
  }
}