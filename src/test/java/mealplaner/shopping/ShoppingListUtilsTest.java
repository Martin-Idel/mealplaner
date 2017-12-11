package mealplaner.shopping;

import static mealplaner.commons.NonnegativeInteger.FOUR;
import static mealplaner.commons.NonnegativeInteger.THREE;
import static mealplaner.commons.Pair.of;
import static mealplaner.shopping.ShoppingList.from;
import static mealplaner.shopping.ShoppingListUtils.createShoppingList;
import static mealplaner.shopping.ShoppingListUtils.missingRecipesForCompleteList;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getRecipe1;
import static testcommons.CommonFunctions.getRecipe2;
import static testcommons.CommonFunctions.proposal1;
import static testcommons.CommonFunctions.proposal2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.Proposal;
import mealplaner.recipes.model.Recipe;

public class ShoppingListUtilsTest {

  @Test
  public void missingRecipesForCompleteListFindsMissingRecipe() {
    Proposal testProposal = proposal1();

    assertThat(missingRecipesForCompleteList(testProposal)).isTrue();
  }

  @Test
  public void missingRecipesForCompleteListOkIfAllRecipesArePresent() {
    Proposal testProposal = proposal2();

    assertThat(missingRecipesForCompleteList(testProposal)).isFalse();
  }

  @Test
  public void correctlyAvoidsMissingRecipesWhenPrinting() {
    Proposal testProposal = proposal1();
    List<Pair<Recipe, NonnegativeInteger>> listInProposal = new ArrayList<>();
    listInProposal.add(of(getRecipe1(), FOUR));

    ShoppingList shoppingList = createShoppingList(testProposal);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(from(listInProposal).getMap());
    assertThat(from(listInProposal).getMap()).containsAllEntriesOf(shoppingList.getMap());
  }

  @Test
  public void correctlyAccumulatesRecipes() {
    Proposal testProposal = proposal2();
    List<Pair<Recipe, NonnegativeInteger>> listInProposal = new ArrayList<>();
    listInProposal.add(of(getRecipe1(), THREE));
    listInProposal.add(of(getRecipe2(), FOUR));

    ShoppingList shoppingList = createShoppingList(testProposal);

    assertThat(shoppingList.getMap()).containsAllEntriesOf(from(listInProposal).getMap());
    assertThat(from(listInProposal).getMap()).containsAllEntriesOf(shoppingList.getMap());
  }
}
