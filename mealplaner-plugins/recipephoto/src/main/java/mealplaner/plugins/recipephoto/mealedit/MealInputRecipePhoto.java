// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import static java.util.Collections.singletonList;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.plugins.recipephoto.mealedit.RecipePhotoEdit.copyAndSaveImage;

import java.io.File;
import java.util.List;

import mealplaner.commons.gui.inputfields.ButtonInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;

public class MealInputRecipePhoto implements MealInputDialogExtension {
  private static final String SAVE_PATH = System.getProperty("user.home")
      + File.separator + ".mealplaner" + File.separator + "photos";

  @Override
  public List<InputField<MealFact>> createInputElements() {
    return singletonList(new ButtonInputField<>(
        BUNDLES.message("insertPhotoText"),
        "InsertPhoto",
        BUNDLES.message("changePhotoButton"),
        BUNDLES.message("noPhotoButton"),
        new RecipePhotoFact(),
        (fact) -> copyAndSaveImage((RecipePhotoFact) fact, SAVE_PATH),
        150));
  }

}
