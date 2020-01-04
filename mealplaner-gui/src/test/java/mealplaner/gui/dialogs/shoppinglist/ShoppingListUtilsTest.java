// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.shoppinglist;

import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.Pair.of;
import static mealplaner.gui.dialogs.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.gui.dialogs.shopping.ShoppingListUtils.someRecipesMissingForCompleteList;
import static mealplaner.model.MealplanerData.getInstance;
import static mealplaner.model.shoppinglist.ShoppingList.from;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.getMeal1;
import static testcommons.CommonBaseFunctions.getMeal2;
import static testcommons.CommonBaseFunctions.getMeal3;
import static testcommons.CommonBaseFunctions.getProposal1;
import static testcommons.CommonBaseFunctions.getProposal2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.shoppinglist.ShoppingList;
import mealplaner.plugins.PluginStore;

public class ShoppingListUtilsTest {
  private final MealplanerData data = getInstance(new PluginStore());

  @Before
  public void setUp() {
    data.clear();
  }

  @Test
  public void someRecipesMissingForCompleteListFindsMissingRecipe() {
    List<Meal> meals = initialiseProposalMeals();
    data.setMeals(meals);

    Proposal testProposal = getProposal1();

    assertThat(someRecipesMissingForCompleteList(testProposal, data)).isTrue();
  }

  @Test
  public void someRecipesMissingForCompleteListOkIfAllRecipesArePresent() {
    List<Meal> meals = initialiseProposal2Meals();
    data.setMeals(meals);

    Proposal testProposal = getProposal2();

    assertThat(someRecipesMissingForCompleteList(testProposal, data)).isFalse();
  }

  @Test
  public void correctlyAvoidsMissingRecipesWhenPrinting() {
    List<Meal> meals = initialiseProposalMeals();
    data.setMeals(meals);

    Proposal testProposal = getProposal1();
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
    Proposal testProposal = getProposal2();
    List<Pair<Recipe, NonnegativeInteger>> listInProposal = new ArrayList<>();
    listInProposal.add(of(meals.get(0).getRecipe().get(), THREE));
    listInProposal.add(of(meals.get(1).getRecipe().get(), FOUR));

    ShoppingList shoppingList = createShoppingList(testProposal, data);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(from(listInProposal).getMap());
    assertThat(from(listInProposal).getMap()).containsAllEntriesOf(shoppingList.getMap());
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
