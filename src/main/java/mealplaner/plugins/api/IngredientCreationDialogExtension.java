package mealplaner.plugins.api;

import java.util.List;

import mealplaner.commons.gui.inputfields.InputField;

public interface IngredientCreationDialogExtension {
  List<InputField<IngredientFact>> createInputElements();
}
