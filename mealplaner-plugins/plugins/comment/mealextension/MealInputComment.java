package mealplaner.plugins.plugins.comment.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Collections;
import java.util.List;

import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.TextFactInputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputComment implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return Collections.singletonList(
        new TextFactInputField<>(BUNDLES.message("insertMealComment"), "Comment", 70, CommentFact::new));
  }
}
