package mealplaner.plugins.utensil.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Collections;
import java.util.List;

import mealplaner.commons.gui.inputfields.ComboBoxFactInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputObligatoryUtensil implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return Collections.singletonList(new ComboBoxFactInputField<>(
        BUNDLES.message("insertMealUtensil"),
        "ObligatoryUtensil",
        ObligatoryUtensil.class,
        ObligatoryUtensil.POT, 30,
        ObligatoryUtensilFact::new));
  }
}
