package mealplaner.gui.dialogs.mealinput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public class MultipleMealInput extends MealInput {
  private final List<Meal> newMeals;

  public MultipleMealInput(JFrame parent) {
    super(parent);
    newMeals = new ArrayList<>();
  }

  public List<Meal> showDialog(IngredientProvider ingredientProvider) {
    display(ingredientProvider, action -> saveMeal());
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
