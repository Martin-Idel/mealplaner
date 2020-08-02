package mealplaner.plugins.vegetarian.ingredientextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Collections;
import java.util.List;

import mealplaner.commons.gui.inputfields.ButtonInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientInputDialogExtension;

public class SeasonalFactInput implements IngredientInputDialogExtension {
  @Override
  public List<InputField<IngredientFact>> createInputElements() {
    return Collections.singletonList(new ButtonInputField<>(
        BUNDLES.message("seasonsLabel"),
        "SeasonsFact",
        BUNDLES.message("seasonsLabelEditContent"),
        BUNDLES.message("seasonsLabelNoContent"),
        new SeasonalFact(), content -> new SeasonalFact(), 100));
  }

  private SeasonalFact editSeasonalFactDialog(SeasonalFact seasonalFact) {
    return seasonalFact;
  }

}
