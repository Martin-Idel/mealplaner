// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.inputfields.InputField;

public interface MealInputDialogExtension {
  List<InputField<MealFact>> createInputElements();
}
