package mealplaner.gui.dialogs.shopping;

import static mealplaner.commons.Pair.of;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.DataStore;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.shoppinglist.ShoppingList;

public final class ShoppingListUtils {

  private ShoppingListUtils() {
  }

  public static boolean someRecipesMissingForCompleteList(Proposal proposal,
      DataStore data) {
    return proposal.getProposalList()
        .stream()
        .filter(meal -> !allMealsHaveRecipes(meal, data))
        .findAny()
        .isPresent();
  }

  private static boolean allMealsHaveRecipes(ProposedMenu proposedMenu, DataStore data) {
    return hasRecipe(proposedMenu.entry, data)
        && hasRecipe(proposedMenu.main, data)
        && hasRecipe(proposedMenu.desert, data);
  }

  private static boolean hasRecipe(Optional<UUID> uuid, DataStore data) {
    return !uuid.isPresent() || hasRecipe(uuid.get(), data);
  }

  private static boolean hasRecipe(UUID uuid, DataStore data) {
    return data.getMeal(uuid)
        .flatMap(meal -> meal.getRecipe())
        .isPresent();
  }

  public static ShoppingList createShoppingList(Proposal proposal, DataStore dataStore) {
    return ShoppingList.from(getAllRecipesInProposal(proposal, dataStore));
  }

  private static List<Pair<Recipe, NonnegativeInteger>> getAllRecipesInProposal(Proposal proposal,
      DataStore data) {
    List<ProposedMenu> meals = proposal.getProposalList();
    List<Pair<Recipe, NonnegativeInteger>> allRecipes = new ArrayList<>();
    for (ProposedMenu proposed : meals) {
      Optional<Recipe> entryRecipe = getRecipe(proposed.entry, data);
      entryRecipe.ifPresent(recipe -> allRecipes.add(of(recipe, proposed.numberOfPeople)));
      Optional<Recipe> mainRecipe = getRecipe(Optional.of(proposed.main), data);
      mainRecipe.ifPresent(recipe -> allRecipes.add(of(recipe, proposed.numberOfPeople)));
      Optional<Recipe> desertRecipe = getRecipe(proposed.desert, data);
      desertRecipe.ifPresent(recipe -> allRecipes.add(of(recipe, proposed.numberOfPeople)));
    }
    return allRecipes;
  }

  private static Optional<Recipe> getRecipe(Optional<UUID> mealId, DataStore data) {
    return mealId
        .flatMap(id -> data.getMeal(id))
        .flatMap(meal -> meal.getRecipe());
  }
}
