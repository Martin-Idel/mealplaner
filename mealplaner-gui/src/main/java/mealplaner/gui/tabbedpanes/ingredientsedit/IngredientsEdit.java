// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.Utils.not;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.tables.TableHelpers.deleteSelectedRows;
import static mealplaner.gui.dialogs.ingredients.IngredientsInput.ingredientsInput;
import static mealplaner.gui.tabbedpanes.ingredientsedit.IngredientsEditTable.createTable;
import static mealplaner.gui.tabbedpanes.ingredientsedit.ReplaceIngredientDialog.showReplaceDialog;
import static mealplaner.io.DataParts.INGREDIENTS;
import static mealplaner.model.DataStoreEventType.INGREDIENTS_CHANGED;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.io.FileIoGui;
import mealplaner.model.DataStoreEventType;
import mealplaner.model.DataStoreListener;
import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Ingredient;
import mealplaner.plugins.PluginStore;

public class IngredientsEdit implements DataStoreListener {
  private final MealplanerData mealPlan;
  private final JFrame frame;
  private final JPanel dataPanel;
  private final FileIoGui fileIoGui;

  private final List<Ingredient> ingredients = new ArrayList<>();

  private Table table;
  private ButtonPanelEnabling buttonPanel;

  public IngredientsEdit(MealplanerData mealPlan, JFrame frame, JPanel ingredientsPanel,
      FileIoGui fileIoGui) {
    this.mealPlan = mealPlan;
    this.frame = frame;
    this.dataPanel = ingredientsPanel;
    this.fileIoGui = fileIoGui;
    this.mealPlan.register(this);

    dataPanel.setLayout(new BorderLayout());
  }

  public void setupPane(PluginStore pluginStore) {
    buttonPanel = createButtonPanelWithEnabling(pluginStore);
    buttonPanel.disableButtons();

    ingredients.addAll(mealPlan.getIngredients());
    table = createTable(ingredients, buttonPanel, pluginStore.getRegisteredIngredientEditGuiExtensions());

    dataPanel.add(table.getComponent(), BorderLayout.CENTER);
    dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
  }

  private ButtonPanelEnabling createButtonPanelWithEnabling(PluginStore pluginStore) {
    return builder("IngredientsEdit")
        .addAddButton(action -> insertItems(ingredientsInput(frame).showDialog(mealPlan, pluginStore)))
        .addRemoveSelectedButton(action -> {
          deleteSelectedRows(table, number -> ingredients.remove((int) number));
          buttonPanel.enableButtons();
        })
        .addSaveButton(action -> {
          saveIngredients();
          buttonPanel.disableButtons();
        })
        .makeLastButtonEnabling()
        .addCancelButton(action -> updateTable())
        .makeLastButtonEnabling()
        .buildEnablingPanel();
  }

  public void saveIngredients() {
    List<Ingredient> deletedIngredientsStillInUse = mealPlan
        .deletedIngredientsStillInUse(ingredients);
    deletedIngredientsStillInUse.forEach(replaceIngredientOrDoNotDelete());
    mealPlan.setIngredients(ingredients);
    fileIoGui.savePart(mealPlan, INGREDIENTS);
  }

  private Consumer<Ingredient> replaceIngredientOrDoNotDelete() {
    return ingredient -> {
      Optional<Ingredient> replaced = showReplaceDialog(frame, ingredients, ingredient);
      if (replaced.isPresent()) {
        mealPlan.replaceIngredient(ingredient, replaced.get());
      } else {
        ingredients.add(ingredient);
      }
    };
  }

  private void updateTable() {
    List<Ingredient> newIngredients = mealPlan.getIngredients();
    ingredients.removeAll(ingredients
        .stream()
        .filter(not(newIngredients::contains))
        .collect(toList()));
    newIngredients.stream()
        .filter(not(ingredients::contains))
        .forEach(ingredients::add);
    ingredients
        .sort(Comparator.comparing(Ingredient::getName));
    table.update();
    buttonPanel.disableButtons();
  }

  private void insertItems(List<Ingredient> newIngredient) {
    newIngredient.forEach(this::addAtSortedPosition);
  }

  private void addAtSortedPosition(Ingredient ingredient) {
    int row = 0;
    while (row < ingredients.size()
        && ingredient.getName().compareTo(ingredients.get(row).getName()) >= 0) {
      row++;
    }
    ingredients.add(row, ingredient);
    table.insertRows(row, row);
    buttonPanel.enableButtons();
  }

  @Override
  public void updateData(DataStoreEventType event) {
    if (event.equals(INGREDIENTS_CHANGED)) {
      updateTable();
    }
  }
}
