package mealplaner.model;

import static java.time.LocalDate.of;
import static java.util.Optional.empty;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.model.DataStoreEventType.DATE_UPDATED;
import static mealplaner.model.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.model.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.meal.Meal.createMeal;
import static mealplaner.model.proposal.Proposal.createProposal;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.DataStoreListener;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;

@RunWith(MockitoJUnitRunner.class)
public class MealplanerDataTest {
  private final List<Meal> meals = new ArrayList<>();
  private Meal meal1;
  private Meal meal3;
  private Meal meal4;
  private final List<Ingredient> ingredients = new ArrayList<>();
  private DataStoreListener listener;
  private LocalDate date;
  private Proposal proposal;

  private final MealplanerData sut = getInstance();

  @Before
  public void setUp() {
    addInitializedMeals();
    date = of(2017, 5, 7);
    proposal = createProposal();
    ingredients.addAll(createIngredientsList());
    sut.clear();
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
    List<Meal> meals = new ArrayList<>();
    meals.add(initializeNewMeal());

    sut.setMeals(meals);

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
    List<ProposedMenu> proposalMeals = new ArrayList<>();
    proposalMeals.add(mainOnly(meal1.getId(), TWO));
    proposalMeals.add(mainOnly(meal4.getId(), TWO));
    proposal = from(true, proposalMeals);
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
}
