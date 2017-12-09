package mealplaner.shopping;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.Pair.of;

import java.util.Optional;

import mealplaner.model.Proposal;

public final class ShoppingListUtils {

	private ShoppingListUtils() {
	}

	public static boolean missingRecipesForCompleteList(Proposal proposal) {
		return proposal.getProposalList()
				.stream()
				.filter(meal -> !meal.getRecipe().isPresent())
				.findAny()
				.isPresent();
	}

	public static ShoppingList createShoppingList(Proposal proposal) {
		return ShoppingList.from(proposal.getMealsAndSettings().stream()
				.map(pair -> of(pair.left.getRecipe(), pair.right.getNumberOfPeople()))
				.filter(pair -> pair.left.isPresent())
				.map(pair -> pair.mapLeft(Optional::get))
				.collect(toList()));
	}

}
