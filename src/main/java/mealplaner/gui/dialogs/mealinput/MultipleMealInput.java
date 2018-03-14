package mealplaner.gui.dialogs.mealinput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.model.Ingredient;

public class MultipleMealInput extends MealInput<List<Meal>> {
  private final List<Meal> newMeals;

  public MultipleMealInput(JFrame parent) {
    super(parent);
    newMeals = new ArrayList<>();
  }

  @Override
  public List<Meal> showDialog(List<Ingredient> ingredients) {
    display(ingredients, action -> saveMeal());
    dispose();
    return newMeals;
  }

  private void saveMeal() {
    Optional<Meal> mealFromInput = getMealAndShowDialog();
    mealFromInput.ifPresent(meal -> {
      newMeals.add(meal);
      resetFields();
    });
  }
}
