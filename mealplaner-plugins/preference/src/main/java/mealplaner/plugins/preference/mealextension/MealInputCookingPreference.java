package mealplaner.plugins.preference.mealextension;

import static java.util.Collections.singletonList;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.List;

import mealplaner.commons.gui.inputfields.ComboBoxFactInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputCookingPreference implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return singletonList(new ComboBoxFactInputField<>(
        BUNDLES.message("insertMealPopularity"),
        "CookingPreference",
        CookingPreference.class,
        CookingPreference.NO_PREFERENCE, 50,
        CookingPreferenceFact::new));
  }
}
