package mealplaner.shopping;

import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import mealplaner.commons.Pair;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
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

	public Map<Ingredient, Integer> getSubsetFor(IngredientType... types) {
		List<IngredientType> ingredientTypes = Arrays.asList(types);
		return shoppingList.entrySet().stream()
				.filter(entry -> ingredientTypes.contains(entry.getKey().getType()))
				.collect(toMap(Entry::getKey, Entry::getValue));
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