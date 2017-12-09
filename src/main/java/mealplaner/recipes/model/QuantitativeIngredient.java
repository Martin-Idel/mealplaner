package mealplaner.recipes.model;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Ingredient.emptyIngredient;

import mealplaner.commons.NonnegativeInteger;

public class QuantitativeIngredient {
	public static final QuantitativeIngredient DEFAULT = new QuantitativeIngredient(
			emptyIngredient(), nonNegative(0));

	private final Ingredient ingredient;
	private final NonnegativeInteger amount;

	QuantitativeIngredient(Ingredient ingredient,
			NonnegativeInteger amount) {
		this.ingredient = ingredient;
		this.amount = amount;
	}

	public static QuantitativeIngredient create(Ingredient ingredient, NonnegativeInteger amount) {
		return new QuantitativeIngredient(ingredient, amount);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public NonnegativeInteger getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "[ " + ingredient.toString() + ", "
				+ amount.toString() + "]";
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((amount == null) ? 0 : amount.hashCode());
		result = 31 * result + ((ingredient == null) ? 0 : ingredient.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		QuantitativeIngredient other = (QuantitativeIngredient) obj;
		if (!ingredient.equals(other.ingredient) ||
				!amount.equals(other.amount)) {
			return false;
		}
		return true;
	}
}
