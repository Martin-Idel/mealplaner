package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.QuantitativeIngredientBuilder.builder;

import java.util.List;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.tables.Table;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;
import mealplaner.recipes.provider.IngredientProvider;

public class RecipeTable {
	private NonnegativeInteger numberOfPortions;
	private IngredientProvider ingredientProvider;
	private List<QuantitativeIngredient> ingredients;

	public RecipeTable(Recipe recipe, IngredientProvider ingredientProvider) {
		this.numberOfPortions = nonNegative(recipe.getNumberOfPortions());
		this.ingredientProvider = ingredientProvider;
		this.ingredients = recipe.getIngredientsFor(recipe.getNumberOfPortions()).entrySet()
				.stream()
				.map(entry -> builder()
						.ingredient(entry.getKey())
						.amount(nonNegative(entry.getValue()))
						.forPeople(numberOfPortions)
						.build())
				.sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getName()
						.compareTo(ingredient2.getIngredient().getName()))
				.collect(toList());
	}

	public Table setupTable() {
		return IngredientsTable.setupTable(ingredients, ingredientProvider);
	}

	public Optional<Recipe> getRecipe(NonnegativeInteger numberOfPeople) {
		this.numberOfPortions = numberOfPeople;
		if (ingredients.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(Recipe.from(numberOfPeople.value, ingredients));
		}
	}
}
