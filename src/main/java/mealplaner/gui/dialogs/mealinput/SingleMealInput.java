package mealplaner.gui.dialogs.mealinput;

import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.BundleStore;
import mealplaner.model.Meal;

public class SingleMealInput extends MealInput {
	private static final long serialVersionUID = 1L;
	private Meal newMeal;

	public SingleMealInput(JFrame parent, BundleStore bundles) {
		super(parent, bundles);
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
