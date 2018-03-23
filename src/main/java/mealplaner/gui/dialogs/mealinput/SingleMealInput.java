package mealplaner.gui.dialogs.mealinput;

import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.DataStore;
import mealplaner.model.Meal;

public class SingleMealInput extends MealInput<Meal> {
  private Meal newMeal;

  public SingleMealInput(JFrame parent) {
    super(parent);
  }

  @Override
  public Meal showDialog(DataStore store) {
    display(store, action -> saveMeal());
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
