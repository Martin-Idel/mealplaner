// SPDX-License-Identifier: MIT

package mealplaner.model;

import static java.time.LocalDate.of;
import static mealplaner.commons.NonnegativeInteger.TWO;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.model.DataStoreEventType.DATE_UPDATED;
import static mealplaner.model.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.model.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.meal.MealBuilder.meal;
import static mealplaner.model.proposal.Proposal.createProposal;
import static mealplaner.model.proposal.Proposal.from;
import static mealplaner.model.proposal.ProposedMenu.mainOnly;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static testcommonsmodel.CommonBaseFunctions.getIngredient1;
import static testcommonsmodel.CommonBaseFunctions.getIngredient2;
import static testcommonsmodel.CommonBaseFunctions.getIngredient3;
import static testcommonsmodel.CommonBaseFunctions.getMeal2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.MealBuilder;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;

@ExtendWith(MockitoExtension.class)
public class MealplanerDataTest {
  private final List<Meal> meals = new ArrayList<>();
  private Meal meal1;
  private Meal meal3;
  private Meal meal4;
  private final List<Ingredient> ingredients = new ArrayList<>();
  private DataStoreListener listener;
  private LocalDate date;
  private Proposal proposal;
  private final PluginStore pluginStore = new PluginStore();

  private final MealplanerData sut = getInstance(pluginStore);

  @BeforeEach
  public void setUp() {
    addInitializedMeals();
    date = of(2017, 5, 7);
    proposal = createProposal();
    ingredients.addAll(createIngredientsList());
    sut.clear();
    sut.setIngredients(ingredients);
    sut.setMeals(meals);
    sut.setTime(date);
    sut.setDefaultSettings(createDefaultSettings(pluginStore));
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

    sut.setDefaultSettings(createDefaultSettings(pluginStore));

    verify(listener).updateData(SETTINGS_CHANGED);
  }

  @Test
  public void setIngredientsGuardsAgainstAddingFurtherIngredients() {
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
  public void setIngredientsThrowsExceptionIfIngredientIsDeletedButStillInUse() {
    sut.addMeal(getMeal2());

    Assertions.assertThrows(MealException.class, () -> sut.setIngredients(new ArrayList<>()));
  }

  @Test
  public void deletedIngredientsStillInUseReturnsNonemptyListIfIngredientIsStillUsed() {
    sut.addMeal(getMeal2());

    List<Ingredient> deletedIngredientsStillInUse = sut
        .deletedIngredientsStillInUse(new ArrayList<>());

    assertThat(deletedIngredientsStillInUse).containsExactlyInAnyOrder(getIngredient1(), getIngredient2());
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
    meal1 = MealBuilder.meal()
        .name("Meal1")
        .addDaysPassed(nonNegative(50))
        .create();
    meals.add(meal1);
    Meal meal2 = meal()
        .name("Meal2")
        .addDaysPassed(nonNegative(101))
        .create();
    meals.add(meal2);
    meal4 = meal()
        .name("Meal4")
        .addDaysPassed(nonNegative(20))
        .create();
    meals.add(meal4);
  }

  private Meal initializeNewMeal() {
    return meal()
        .name("Meal3")
        .addDaysPassed(nonNegative(10))
        .create();
  }

  private List<Ingredient> createIngredientsList() {
    List<Ingredient> ingredientList = new ArrayList<>();
    ingredientList.add(getIngredient1());
    ingredientList.add(getIngredient2());
    ingredientList.add(getIngredient3());
    return ingredientList;
  }
}
