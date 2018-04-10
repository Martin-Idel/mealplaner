package mealplaner.gui.dialogs.mealinput;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.model.DataStore;
import mealplaner.model.meal.Meal;

public class MultipleMealInput extends MealInput<List<Meal>> {
  private final List<Meal> newMeals;

  public MultipleMealInput(JFrame parent) {
    super(parent);
    newMeals = new ArrayList<>();
  }

  @Override
  public List<Meal> showDialog(DataStore store) {
    display(store, action -> saveMeal());
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
