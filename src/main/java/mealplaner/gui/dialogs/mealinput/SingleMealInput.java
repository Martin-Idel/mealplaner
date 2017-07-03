package mealplaner.gui.dialogs.mealinput;

import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import mealplaner.model.Meal;

public class SingleMealInput extends MealInput {
	private static final long serialVersionUID = 1L;
	private Meal newMeal;

	public SingleMealInput(JFrame parent, ResourceBundle parentMessages) {
		super(parent, parentMessages);
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
