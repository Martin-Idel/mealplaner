package mealplaner.shopping;

import static java.time.LocalDate.of;
import static mealplaner.MealplanerData.getInstance;
import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.Pair.of;
import static mealplaner.shopping.ShoppingList.from;
import static mealplaner.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.shopping.ShoppingListUtils.someRecipesMissingForCompleteList;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mealplaner.MealplanerData;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.model.Recipe;

public class ShoppingListUtilsTest {
  private MealplanerData data;

  @Before
  public void setUp() {
    data = getInstance();
    data.clear();
  }

  @Test
  public void someRecipesMissingForCompleteListFindsMissingRecipe() {
    List<Meal> meals = initialiseProposalMeals();
    data.setMeals(meals);

    Proposal testProposal = Proposal.from(true, meals, getSettingsList(), of(2017, 7, 5));

    assertThat(someRecipesMissingForCompleteList(testProposal, data)).isTrue();
  }

  @Test
  public void someRecipesMissingForCompleteListOkIfAllRecipesArePresent() {
    List<Meal> meals = initialiseProposal2Meals();
    data.setMeals(meals);

    Proposal testProposal = Proposal.from(true, meals, getSettingsList(), of(2017, 7, 5));

    assertThat(someRecipesMissingForCompleteList(testProposal, data)).isFalse();
  }

  @Test
  public void correctlyAvoidsMissingRecipesWhenPrinting() {
    List<Meal> meals = initialiseProposalMeals();
    data.setMeals(meals);
    Proposal testProposal = Proposal.from(true, meals, getSettingsList(), of(2017, 7, 5));
    List<Pair<Recipe, NonnegativeInteger>> listInProposal = new ArrayList<>();
    listInProposal.add(of(meals.get(1).getRecipe().get(), FOUR));

    ShoppingList shoppingList = createShoppingList(testProposal, data);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(from(listInProposal).getMap());
    assertThat(from(listInProposal).getMap()).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void correctlyAccumulatesRecipes() {
    List<Meal> meals = initialiseProposal2Meals();
    data.setMeals(meals);
    Proposal testProposal = Proposal.from(true, meals, getSettingsList(), of(2017, 7, 5));
    List<Pair<Recipe, NonnegativeInteger>> listInProposal = new ArrayList<>();
    listInProposal.add(of(meals.get(0).getRecipe().get(), THREE));
    listInProposal.add(of(meals.get(1).getRecipe().get(), FOUR));

    ShoppingList shoppingList = createShoppingList(testProposal, data);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(from(listInProposal).getMap());
    assertThat(from(listInProposal).getMap()).containsAllEntriesOf(shoppingList.getMap());
  }

  private List<Settings> getSettingsList() {
    List<Settings> settings = new ArrayList<>();
    settings.add(getSettings1());
    settings.add(getSettings2());
    return settings;
  }

  private List<Meal> initialiseProposalMeals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    return meals;
  }

  private List<Meal> initialiseProposal2Meals() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal2());
    meals.add(getMeal3());
    return meals;
  }
}
