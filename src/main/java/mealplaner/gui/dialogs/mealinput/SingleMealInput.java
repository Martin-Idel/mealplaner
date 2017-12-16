package mealplaner.gui.dialogs.mealinput;

import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public class SingleMealInput extends MealInput<Meal> {
  private Meal newMeal;

  public SingleMealInput(JFrame parent) {
    super(parent);
  }

  @Override
  public Meal showDialog(IngredientProvider ingredientProvider) {
    display(ingredientProvider, action -> saveMeal());
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
