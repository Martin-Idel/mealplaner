// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Collections;
import java.util.List;

import mealplaner.commons.gui.inputfields.CheckboxFactInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputVegetarian implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return Collections.singletonList(
        new CheckboxFactInputField<>(
        BUNDLES.message("insertMealVegetarian"),
        "Vegetarian",
        65, VegetarianFact::new));
  }
}
