package mealplaner.recipes.gui.dialogs.ingredients;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.recipes.model.Ingredient.ingredient;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.commons.gui.GridPanel;
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
    allFields().forEach(field -> field.addToPanel(ingredientCreationPanel.getPanel()));

    JPanel buttonPanel = setupButtonPanel(saveListener);

    dialogWindow.addCentral(ingredientCreationPanel.getPanel());
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    resetFields();
    dialogWindow.setVisible();
  }

  private void setupInputFields() {
    nameField = new NonEmptyTextInputField(BUNDLES.message("insertIngredientName"));
    typeField = new ComboBoxInputField<IngredientType>(
        BUNDLES.message("insertTypeLength"),
        IngredientType.class,
        IngredientType.OTHER);
    measureField = new ComboBoxInputField<Measure>(
        BUNDLES.message("insertMeasure"),
        Measure.class,
        Measure.NONE);
  }

  private JPanel setupButtonPanel(ActionListener saveListener) {
    return new ButtonPanelBuilder()
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
