package mealplaner.gui.dialogs.mealinput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.Meal;
import mealplaner.recipes.provider.IngredientProvider;

public class MultipleMealInput extends MealInput {
  private static final long serialVersionUID = 1L;
  private final List<Meal> newMeals;

  public MultipleMealInput(JFrame parent, IngredientProvider ingredientProvider) {
    super(parent, ingredientProvider);
    newMeals = new ArrayList<>();
  }

  public List<Meal> showDialog() {
    display(action -> saveMeal());
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
