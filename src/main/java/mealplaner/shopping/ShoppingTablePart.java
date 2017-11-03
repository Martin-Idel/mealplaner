package mealplaner.shopping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;

public class ShoppingTablePart {
	private Set<IngredientType> types;
	private Map<Ingredient, Integer> ingredients;

	private ShoppingTablePart(Map<Ingredient, Integer> ingredients, IngredientType... types) {
		this.ingredients = new HashMap<>();
		ingredients.forEach(this.ingredients::put);
		this.types = new HashSet<>();
		this.types.addAll(Arrays.asList(types));
	}

	public static ShoppingTablePart from(ShoppingList shoppingList, IngredientType... types) {
		return new ShoppingTablePart(shoppingList.getSubsetFor(types), types);
	}

	public boolean isPartOf(Ingredient ingredient) {
		return types.contains(ingredient.getType());
	}

	public int size() {
		return ingredients.size();
	}

	public boolean contained(Ingredient ingredient) {
		return ingredients.containsKey(ingredient);
	}

	public void changeIngredient(Ingredient ingredient, int amount) {
		if (isPartOf(ingredient)) {
			ingredients.compute(ingredient,
					(key, value) -> value == null ? amount : (value + amount));
		}
	}

	public Integer getAmount(Ingredient ingredient) {
		if (isPartOf(ingredient) && ingredients.containsKey(ingredient)) {
			return ingredients.get(ingredient);
		}
		return 0;
	}

	public void addIngredients(Map<Ingredient, Integer> recipe) {
		recipe.forEach(this::changeIngredient);
	}

	public Map<Ingredient, Integer> getContent() {
		return ingredients;
	}
}
