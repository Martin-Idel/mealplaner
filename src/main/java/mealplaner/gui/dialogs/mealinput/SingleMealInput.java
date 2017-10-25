package mealplaner.gui.dialogs.mealinput;

import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public class SingleMealInput extends MealInput {
	private static final long serialVersionUID = 1L;
	private Meal newMeal;

	public SingleMealInput(JFrame parent, IngredientProvider ingredientProvider) {
		super(parent, ingredientProvider);
	}

	public Meal showDialog() {
		display(action -> saveMeal());
		return newMeal;
	}

	private void saveMeal() {
		Optional<Meal> mealFromInput = getMealAndShowDialog();
		mealFromInput.ifPresent(meal -> {
			newMeal = meal;
			setVisible(false);
			dispose();
		});
	}
}
