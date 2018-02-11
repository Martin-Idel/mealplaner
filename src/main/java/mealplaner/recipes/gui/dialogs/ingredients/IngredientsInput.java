package mealplaner.recipes.gui.dialogs.ingredients;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.recipes.model.Ingredient.ingredient;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.inputfields.ComboBoxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonEmptyTextInputField;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;

public class IngredientsInput {
  private final DialogWindow dialogWindow;

  private InputField<Optional<String>> nameField;
  private InputField<IngredientType> typeField;
  private InputField<Measure> measureField;

  private final List<Ingredient> ingredients;

  public IngredientsInput(JFrame parent) {
    dialogWindow = DialogWindow.window(parent, BUNDLES.message("ingredientInputDialogTitle"));
    ingredients = new ArrayList<>();
  }

  public List<Ingredient> showDialog() {
    setupDialog(action -> {
      if (nameField.getUserInput().isPresent()) {
        ingredients.add(ingredient(nameField.getUserInput().get(),
            typeField.getUserInput(),
            measureField.getUserInput()));
        resetFields();
      }
    });
    dialogWindow.dispose();
    return ingredients;
  }

  private void setupDialog(ActionListener saveListener) {
    setupInputFields();
    GridPanel ingredientCreationPanel = gridPanel(0, 2);
    allFields().forEach(field -> field.addToPanel(ingredientCreationPanel));

    ButtonPanel buttonPanel = setupButtonPanel(saveListener);

    dialogWindow.addCentral(ingredientCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    resetFields();
    dialogWindow.setVisible();
  }

  private void setupInputFields() {
    nameField = new NonEmptyTextInputField(
        BUNDLES.message("insertIngredientName"),
        "IngredientName");
    typeField = new ComboBoxInputField<IngredientType>(
        BUNDLES.message("insertTypeLength"),
        "IngredientType",
        IngredientType.class,
        IngredientType.OTHER);
    measureField = new ComboBoxInputField<Measure>(
        BUNDLES.message("insertMeasure"),
        "IngredientMeasure",
        Measure.class,
        Measure.NONE);
  }

  private ButtonPanel setupButtonPanel(ActionListener saveListener) {
    return builder("IngredientsInput")
        .addSaveButton(saveListener)
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  private void resetFields() {
    Arrays.asList(nameField, typeField, measureField).forEach(InputField::resetField);
  }

  private Stream<InputField<?>> allFields() {
    return Arrays.asList(nameField, typeField, measureField).stream();
  }
}
