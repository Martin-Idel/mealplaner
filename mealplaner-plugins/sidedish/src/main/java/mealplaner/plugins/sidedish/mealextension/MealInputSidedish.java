package mealplaner.plugins.sidedish.mealextension;

import static java.util.Collections.singletonList;
import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.List;

import mealplaner.commons.gui.inputfields.ComboBoxFactInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputSidedish implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return singletonList(new ComboBoxFactInputField<>(
            BUNDLES.message("insertMealSidedish"),
            "Sidedish",
            Sidedish.class,
            Sidedish.NONE,
            20,
            SidedishFact::new));
  }
}
