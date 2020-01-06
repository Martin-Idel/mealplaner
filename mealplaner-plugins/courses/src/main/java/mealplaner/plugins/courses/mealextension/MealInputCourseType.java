// SPDX-License-Identifier: MIT

package mealplaner.plugins.courses.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.util.Collections;
import java.util.List;

import mealplaner.commons.gui.inputfields.ComboBoxFactInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealInputDialogExtension;
import mealplaner.plugins.builtins.courses.CourseType;
import mealplaner.plugins.builtins.courses.CourseTypeFact;

public class MealInputCourseType implements MealInputDialogExtension {
  @Override
  public List<InputField<MealFact>> createInputElements() {
    return Collections.singletonList(new ComboBoxFactInputField<>(
        BUNDLES.message("insertMealCourseType"),
        "CourseType",
        CourseType.class,
        CourseType.MAIN, 60,
        CourseTypeFact::new));
  }
}
