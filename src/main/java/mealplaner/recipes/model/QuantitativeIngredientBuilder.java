package mealplaner.recipes.model;

import mealplaner.commons.NonnegativeInteger;

public class QuantitativeIngredientBuilder {
	private Ingredient ingredient;
	private NonnegativeInteger amount;
	private NonnegativeInteger numberOfPeople;

	private QuantitativeIngredientBuilder(Ingredient ingredient,
			NonnegativeInteger amount,
			NonnegativeInteger numberOfPeople) {
		this.ingredient = ingredient;
		this.amount = amount;
		this.numberOfPeople = numberOfPeople;
	}

	public static QuantitativeIngredientBuilder builder() {
		return new QuantitativeIngredientBuilder(null, null, null);
	}

	public static QuantitativeIngredientBuilder from(QuantitativeIngredient ingredient) {
		return new QuantitativeIngredientBuilder(ingredient.getIngredient(),
				ingredient.getAmountFor(ingredient.getNumberOfPeople()),
				ingredient.getNumberOfPeople());
	}

	public QuantitativeIngredientBuilder ingredient(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public QuantitativeIngredientBuilder amount(NonnegativeInteger amount) {
		this.amount = amount;
		return this;
	}

	public QuantitativeIngredientBuilder forPeople(NonnegativeInteger numberOfPeople) {
		this.numberOfPeople = numberOfPeople;
		return this;
	}

	public QuantitativeIngredient build() {
		return new QuantitativeIngredient(ingredient, amount, numberOfPeople);
	}
}