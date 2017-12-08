package mealplaner.shopping;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mealplaner.commons.Pair;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;

public class ShoppingList {
	private Map<Ingredient, Integer> shoppingList;

	private ShoppingList(List<Pair<Recipe, Integer>> recipes) {
		shoppingList = new HashMap<>();
		recipes.forEach(pair -> addRecipeForNumberOfPeople(pair.left, pair.right));
	}

	public static ShoppingList from(List<Pair<Recipe, Integer>> recipes) {
		return new ShoppingList(recipes);
	}

	public static ShoppingList emptyList() {
		return new ShoppingList(new ArrayList<>());
	}

	public List<QuantitativeIngredient> getList() {
		return shoppingList.entrySet().stream()
				.map(entry -> builder().ingredient(entry.getKey())
						.amount(nonNegative(entry.getValue()))
						.forPeople(nonNegative(1))
						.build())
				.collect(Collectors.toList());
	}

	private void addRecipeForNumberOfPeople(Recipe recipe, int numberOfPeople) {
		recipe.getRecipeFor(numberOfPeople)
				.forEach((ingredient, amount) -> addIngredient(ingredient, amount));
	}

	private void addIngredient(Ingredient ingredient, Integer amount) {
		shoppingList.compute(ingredient,
				(key, value) -> value == null ? amount : (value + amount));
	}

	@Override
	public String toString() {
		return shoppingList.toString();
	}
}
