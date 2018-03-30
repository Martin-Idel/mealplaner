package mealplaner;

import static java.time.LocalDate.of;
import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.model.Proposal.from;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.MealAssert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseType;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.model.Ingredient;

@RunWith(MockitoJUnitRunner.class)
public class MealplanerDataTest {

  private final List<Meal> meals = new ArrayList<>();
  private Meal meal1;
  private Meal meal3;
  private Meal meal4;
  private final List<Ingredient> ingredients = new ArrayList<>();
  private DataStoreListener listener;
  private LocalDate date;

  private MealplanerData sut;
  private Proposal proposal;

  @Before
  public void setUp() {
    addInitializedMeals();
    date = of(2017, 5, 7);
    proposal = from(true, new ArrayList<>(), new ArrayList<>());
    ingredients.addAll(createIngredientsList());
    sut = MealplanerData.getInstance();
    sut.setIngredients(ingredients);
    sut.setMeals(meals);
    sut.setTime(date);
    sut.setDefaultSettings(createDefaultSettings());
    sut.setLastProposal(proposal);

    listener = mock(DataStoreListener.class);
    sut.register(listener);

    meal3 = initializeNewMeal();
  }

  @Test
  public void addMealAtSortedPosition() {

    sut.addMeal(meal3);

    assertThat(sut.getMeals()).asList().hasSize(4);
    assertThat(sut.getMeals().get(2)).isEqualTo(meal3);
  }

  @Test
  public void setMealsNotifiesCorrectListeners() {

    sut.setMeals(proposal.getProposalList());

    verify(listener).updateData(DATABASE_EDITED);
  }

  @Test
  public void addMealNotifiesCorrectListeners() {

    sut.addMeal(meal3);

    verify(listener).updateData(DATABASE_EDITED);
  }

  @Test
  public void updateMealCorrectlyAddsDaysToNonCookedMeals() {

    sut.update(proposal.getProposalList(), date.plusDays(10));

    List<Meal> mealList = sut.getMeals();
    assertThat(mealList.get(0).getDaysPassed()).isEqualByComparingTo(nonNegative(60));
    assertThat(mealList.get(1).getDaysPassed()).isEqualByComparingTo(nonNegative(111));
    assertThat(mealList.get(2).getDaysPassed()).isEqualByComparingTo(nonNegative(30));
  }

  @Test
  public void updateMealCorrectlyUpdatesCookedMeals() {
    List<Meal> proposalMeals = new ArrayList<>();
    proposalMeals.add(meal1);
    proposalMeals.add(meal4);
    proposal = from(true, proposalMeals, createEmptySettingsList(2));
    sut.setMeals(meals);
    sut.setTime(date);
    sut.setLastProposal(proposal);

    sut.update(proposal.getProposalList(), date.plusDays(2));

    List<Meal> mealList = sut.getMeals();
    assertThat(mealList.get(0).getDaysPassed()).isEqualByComparingTo(nonNegative(1));
    assertThat(mealList.get(1).getDaysPassed()).isEqualByComparingTo(nonNegative(103));
    assertThat(mealList.get(2).getDaysPassed()).isEqualByComparingTo(nonNegative(0));
  }

  @Test
  public void updateMealNotifiesCorrectListeners() {

    sut.update(proposal.getProposalList(), date.plusDays(1));

    verify(listener).updateData(DATABASE_EDITED);
    verify(listener).updateData(DATE_UPDATED);
  }

  @Test
  public void setLastProposalNotifiesCorrectListeners() {

    sut.setLastProposal(proposal);

    verify(listener).updateData(PROPOSAL_ADDED);
  }

  @Test
  public void setDefaultSettingsNotifiesCorrectListeners() {

    sut.setDefaultSettings(createDefaultSettings());

    verify(listener).updateData(SETTINGS_CHANGED);
  }

  @Test
  public void setIngredientsProhibitsAddingFurtherIngredients() {
    List<Ingredient> testAgainst = createIngredientsList();

    sut.setIngredients(testAgainst);

    testAgainst.add(getIngredient3());

    assertThat(testAgainst).hasSize(4);
    assertThat(sut.getIngredients()).hasSize(3);
    assertThat(testAgainst).containsAll(sut.getIngredients());
  }

  @Test
  public void getIngredientsGuardsAgainstChangesFromOutside() {
    List<Ingredient> testAgainst = sut.getIngredients();

    testAgainst.add(getIngredient3());

    assertThat(testAgainst).hasSize(4);
    assertThat(sut.getIngredients()).hasSize(3);
    assertThat(testAgainst).containsAll(sut.getIngredients());
  }

  @Test
  public void setMealsProhibitsAddingFurtherIngredients() {
    List<Meal> testAgainst = new ArrayList<>();
    testAgainst.add(meal1);

    sut.setMeals(testAgainst);

    testAgainst.add(meal4);

    assertThat(testAgainst).hasSize(2);
    assertThat(sut.getMeals()).hasSize(1);
    assertThat(testAgainst).containsAll(sut.getMeals());
  }

  @Test
  public void getMealsGuardsAgainstChangesFromOutside() {
    List<Meal> testAgainst = sut.getMeals();

    testAgainst.add(meal4);

    assertThat(testAgainst).hasSize(4);
    assertThat(sut.getMeals()).hasSize(3);
    assertThat(testAgainst).containsAll(sut.getMeals());
  }

  private void addInitializedMeals() throws MealException {
    meal1 = createMeal(randomUUID(), "Meal1", CookingTime.SHORT, Sidedish.NONE,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(50),
        "", empty());
    meals.add(meal1);
    Meal meal2 = createMeal(randomUUID(), "Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
        ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, CourseType.MAIN, nonNegative(101), "",
        empty());
    meals.add(meal2);
    meal4 = createMeal(randomUUID(), "Meal4", CookingTime.LONG, Sidedish.RICE,
        ObligatoryUtensil.POT, CookingPreference.VERY_POPULAR, CourseType.MAIN, nonNegative(20), "",
        empty());
    meals.add(meal4);
  }

  private Meal initializeNewMeal() {
    return createMeal(randomUUID(), "Meal3", CookingTime.SHORT, Sidedish.POTATOES,
        ObligatoryUtensil.PAN, CookingPreference.NO_PREFERENCE, CourseType.MAIN, nonNegative(10),
        "", empty());
  }

  private List<Ingredient> createIngredientsList() {
    List<Ingredient> ingredientList = new ArrayList<>();
    ingredientList.add(getIngredient1());
    ingredientList.add(getIngredient2());
    ingredientList.add(getIngredient3());
    return ingredientList;
  }

  private List<Settings> createEmptySettingsList(int i) {
    List<Settings> settings = new ArrayList<>();
    IntStream.range(0, i).forEach(number -> settings.add(Settings.createSettings()));
    return settings;
  }
}
