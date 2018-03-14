package mealplaner.gui.dialogs.mealinput;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;

public class SingleMealInput extends MealInput<Meal> {
  private Meal newMeal;

  public SingleMealInput(JFrame parent) {
    super(parent);
  }

  @Override
  public Meal showDialog(List<Ingredient> ingredients) {
    display(ingredients, action -> saveMeal());
    return newMeal;
  }

  private void saveMeal() {
    Optional<Meal> mealFromInput = getMealAndShowDialog();
    mealFromInput.ifPresent(meal -> {
      newMeal = meal;
      dispose();
    });
  }
}
