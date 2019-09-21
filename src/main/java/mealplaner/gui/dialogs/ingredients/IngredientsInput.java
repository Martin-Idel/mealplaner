// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.ingredients;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparingInt;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.model.recipes.Ingredient.ingredient;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JFrame;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.commons.gui.inputfields.ComboBoxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonEmptyTextInputField;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.Measures;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.IngredientInputDialogExtension;

public class IngredientsInput implements DialogCreating<List<Ingredient>> {
  private final DialogWindow dialogWindow;

  private InputField<Optional<String>> nameField;
  private InputField<IngredientType> typeField;
  private InputField<Measure> measureField;
  private List<InputField<?>> factInputFields;

  private final List<Ingredient> ingredients;

  public IngredientsInput(JFrame parent) {
    dialogWindow = window(parent, BUNDLES.message("ingredientInputDialogTitle"),
        "IngredientsInputDialog");
    ingredients = new ArrayList<>();
  }

  public IngredientsInput(JDialog parent) {
    dialogWindow = window(parent, BUNDLES.message("ingredientInputDialogTitle"),
        "IngredientsInputDialog");
    ingredients = new ArrayList<>();
  }

  public static IngredientsInput ingredientsInput(JFrame parent) {
    return new IngredientsInput(parent);
  }

  @Override
  public List<Ingredient> showDialog(DataStore store, PluginStore pluginStore) {
    setupDialog(action -> {
      if (nameField.getUserInput().isPresent()) {
        ingredients.add(ingredient(nameField.getUserInput().get(),
            typeField.getUserInput(),
            Measures.createMeasures(measureField.getUserInput())));
        resetFields();
      }
    }, pluginStore.getRegisteredIngredientInputGuiExtensions());
    dialogWindow.dispose();
    return ingredients;
  }

  private void setupDialog(
      ActionListener saveListener, Collection<IngredientInputDialogExtension> ingredientInputDialogExtensions) {
    setupInputFields(ingredientInputDialogExtensions);
    GridPanel ingredientCreationPanel = gridPanel(0, 2);
    allFields().forEach(field -> field.addToPanel(ingredientCreationPanel));

    ButtonPanel buttonPanel = setupButtonPanel(saveListener);

    dialogWindow.addCentral(ingredientCreationPanel);
    dialogWindow.addSouth(buttonPanel);
    dialogWindow.arrangeWithSize(300, 300);
    resetFields();
    dialogWindow.setVisible();
  }

  private void setupInputFields(Collection<IngredientInputDialogExtension> ingredientInputDialogExtensions) {
    nameField = new NonEmptyTextInputField(
        BUNDLES.message("insertIngredientName"),
        "IngredientName", 0);
    typeField = new ComboBoxInputField<>(
        BUNDLES.message("insertTypeLength"),
        "IngredientType",
        IngredientType.class,
        IngredientType.OTHER, 1);
    measureField = new ComboBoxInputField<>(
        BUNDLES.message("insertMeasure"),
        "IngredientMeasure",
        Measure.class,
        Measure.NONE, 2);
    factInputFields = ingredientInputDialogExtensions.stream()
        .flatMap(extension -> extension.createInputElements().stream())
        .sorted(comparingInt(InputField::getOrdering))
        .collect(Collectors.toList());
  }

  private ButtonPanel setupButtonPanel(ActionListener saveListener) {
    return builder("IngredientsInput")
        .addSaveButton(saveListener)
        .addCancelDialogButton(dialogWindow)
        .build();
  }

  private void resetFields() {
    allFields().forEach(InputField::resetField);
  }

  private Stream<InputField<?>> allFields() {
    return Stream.concat(Stream.of(nameField, typeField, measureField), factInputFields.stream());
  }
}
