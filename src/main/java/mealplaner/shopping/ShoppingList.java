package mealplaner.shopping;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredient.create;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mealplaner.commons.Pair;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;

public class ShoppingList {
	private Map<Ingredient, Integer> shoppingList;

	private ShoppingList(Map<Ingredient, Integer> recipes) {
		shoppingList = recipes;
	}

	public static ShoppingList from(List<Pair<Recipe, Integer>> recipes) {
		Map<Ingredient, Integer> shoppingList = recipes.stream()
				.map(pair -> pair.left.getIngredientsFor(pair.right))
				.map(Map::entrySet)
				.flatMap(Collection::stream)
				.collect(toMap(Map.Entry::getKey,
						Map.Entry::getValue,
						(a, b) -> a + b));
		return new ShoppingList(shoppingList);
	}

	public static ShoppingList emptyList() {
		return new ShoppingList(new HashMap<>());
	}

	public List<QuantitativeIngredient> getList() {
		return shoppingList.entrySet().stream()
				.map(entry -> create(entry.getKey(), nonNegative(entry.getValue())))
				.sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getType()
						.compareTo(ingredient2.getIngredient().getType()))
				.collect(toList());
	}

	public Map<Ingredient, Integer> getMap() {
		return shoppingList;
	}
}
