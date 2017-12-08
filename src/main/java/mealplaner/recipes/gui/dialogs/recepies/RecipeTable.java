package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.tables.Table;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public class RecipeTable {
	private List<QuantitativeIngredient> ingredients;
	private IngredientProvider ingredientProvider;
	private NonnegativeInteger numberOfPeople;

	public RecipeTable(Recipe recipe, IngredientProvider ingredientProvider) {
		numberOfPeople = nonNegative(recipe.getNumberOfPortions());
		this.ingredients = recipe.getRecipeFor(recipe.getNumberOfPortions()).entrySet().stream()
				.map(entry -> builder()
						.ingredient(entry.getKey())
						.amount(nonNegative(entry.getValue()))
						.forPeople(numberOfPeople)
						.build())
				.sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getName()
						.compareTo(ingredient2.getIngredient().getName()))
				.collect(toList());
		this.ingredientProvider = ingredientProvider;
	}

	public Table setupTable() {
		return IngredientsTable.setupTable(ingredients, ingredientProvider);
	}

	// TODO: Rewrite with better Recipeinterface
	public Optional<Recipe> getRecipe(NonnegativeInteger numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
		Map<Ingredient, Integer> ing = new HashMap<>();
		ingredients.stream()
				.forEach(ingredient -> {
					if (ing.containsKey(ingredient.getIngredient())) {
						ing.put(ingredient.getIngredient(),
								ing.get(ingredient.getIngredient())
										+ ingredient.getAmountFor(numberOfPeople).value);
					} else {
						ing.put(ingredient.getIngredient(),
								ingredient.getAmountFor(ingredient.getNumberOfPeople()).value);
					}
				});
		if (ing.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(Recipe.from(numberOfPeople.value, ing));
		}
	}
}
