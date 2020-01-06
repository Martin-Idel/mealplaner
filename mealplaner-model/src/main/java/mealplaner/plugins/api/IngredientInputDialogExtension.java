// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.inputfields.InputField;

public interface IngredientInputDialogExtension {
  List<InputField<?>> createInputElements();
}
