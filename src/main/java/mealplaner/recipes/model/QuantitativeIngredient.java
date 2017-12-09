package mealplaner.recipes.model;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.recipes.model.Ingredient.emptyIngredient;

import mealplaner.commons.NonnegativeInteger;

public class QuantitativeIngredient {
	public static final QuantitativeIngredient DEFAULT = new QuantitativeIngredient(
			emptyIngredient(), nonNegative(0), nonNegative(1));

	private final Ingredient ingredient;
	private final NonnegativeInteger amount;
	private final NonnegativeInteger numberOfPeople;

	QuantitativeIngredient(Ingredient ingredient,
			NonnegativeInteger amount,
			NonnegativeInteger numberOfPeople) {
		this.ingredient = ingredient;
		this.amount = amount;
		this.numberOfPeople = numberOfPeople;
	}

	public static QuantitativeIngredient create(Ingredient ingredient, NonnegativeInteger amount,
			NonnegativeInteger numberOfPeople) {
		return new QuantitativeIngredient(ingredient, amount, numberOfPeople);
	}

	public Ingredient getIngredient() {
		return ingredient;
	}

	public NonnegativeInteger getAmountFor(NonnegativeInteger numberOfPeople) {
		return nonNegative((int) (amount.value
				* ((float) numberOfPeople.value / (float) this.numberOfPeople.value)));
	}

	public NonnegativeInteger getAmount() {
		return amount;
	}

	public NonnegativeInteger getNumberOfPeople() {
		return numberOfPeople;
	}

	@Override
	public String toString() {
		return "[ " + ingredient.toString() + ", "
				+ amount.toString() + ", "
				+ numberOfPeople.toString() + "]";
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((amount == null) ? 0 : amount.hashCode());
		result = 31 * result + ((ingredient == null) ? 0 : ingredient.hashCode());
		result = 31 * result + ((numberOfPeople == null) ? 0 : numberOfPeople.hashCode());
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
				!amount.equals(other.amount) ||
				!numberOfPeople.equals(other.numberOfPeople)) {
			return false;
		}
		return true;
	}
}
