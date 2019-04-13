// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static java.text.MessageFormat.format;
import static java.util.Optional.empty;
import static java.util.stream.Collectors.toList;
import static javax.swing.JOptionPane.YES_OPTION;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.inputfields.ComboBoxTextInputField.inputField;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import mealplaner.commons.gui.GuiPanel;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.model.recipes.Ingredient;

final class ReplaceIngredientDialog {
  private ReplaceIngredientDialog() {
  }

  public static Optional<Ingredient> showReplaceDialog(JFrame frame,
      List<Ingredient> ingredients,
      Ingredient ingredient) {

    InputField<String> ingredientField = createInputFieldWithAutoComplete(ingredients, ingredient);
    GuiPanel optionPanel = gridPanel(1, 2);
    ingredientField.addToPanel(optionPanel);

    int result = showDialogForEitherNotDeletingOrReplacing(frame, optionPanel);

    // YES_OPTION == first button pressed
    return result == YES_OPTION ? empty() : findReplacingIngredient(ingredients, ingredientField);
  }

  private static InputField<String> createInputFieldWithAutoComplete(
      List<Ingredient> ingredients, Ingredient ingredient) {
    List<String> ingredientAlternatives = ingredients.stream()
        .map(Ingredient::getName)
        .collect(toList());
    return inputField(
        format(BUNDLES.message("replaceIngredient"), ingredient.getName()),
        "ReplacingIngredient",
        ingredients.isEmpty() ? "" : ingredients.get(0).getName(),
        ingredientAlternatives);
  }

  private static int showDialogForEitherNotDeletingOrReplacing(JFrame frame, GuiPanel optionPanel) {
    Object[] options = { BUNDLES.message("donotDeleteIngredientButton"),
        BUNDLES.message("replaceIngredientButton") };
    return JOptionPane.showOptionDialog(frame,
        optionPanel.getComponent(),
        BUNDLES.message("replaceIngredientDialogTitle"),
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);
  }

  private static Optional<Ingredient> findReplacingIngredient(List<Ingredient> ingredients,
      InputField<String> ingredientField) {
    return ingredients.stream()
        .filter(ing -> ing.getName().equals(ingredientField.getUserInput()))
        .findFirst();
  }
}
