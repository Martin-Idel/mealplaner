package mealplaner.gui.tabbedpanes.ingredientsedit;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.Utils.not;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.commons.gui.tables.TableHelpers.deleteSelectedRows;
import static mealplaner.gui.dialogs.ingredients.IngredientsInput.ingredientsInput;
import static mealplaner.gui.tabbedpanes.ingredientsedit.ReplaceIngredientDialog.showReplaceDialog;
import static mealplaner.io.DataParts.INGREDIENTS;
import static mealplaner.model.DataStoreEventType.INGREDIENTS_CHANGED;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.io.FileIoGui;
import mealplaner.model.DataStoreEventType;
import mealplaner.model.DataStoreListener;
import mealplaner.model.MealplanerData;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

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

  public void setupPane() {
    buttonPanel = createButtonPanelWithEnabling();
    buttonPanel.disableButtons();

    table = createTable();

    dataPanel.add(table.getComponent(), BorderLayout.CENTER);
    dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
  }

  private Table createTable() {
    ingredients.addAll(mealPlan.getIngredients());
    return createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("insertIngredientName"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getName())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newName) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    newName,
                    oldIngredient.getType(),
                    oldIngredient.getMeasure()))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(IngredientType.class)
            .withColumnName(BUNDLES.message("insertTypeLength"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getType())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newType) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    oldIngredient.getName(),
                    newType,
                    oldIngredient.getMeasure()))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("insertMeasure"))
            .getValueFromOrderedList(ingredients, ingredient -> ingredient.getMeasure())
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newMeasure) -> ingredientWithUuid(
                    oldIngredient.getId(),
                    oldIngredient.getName(),
                    oldIngredient.getType(),
                    newMeasure))
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .buildTable();
  }

  private ButtonPanelEnabling createButtonPanelWithEnabling() {
    return builder("IngredientsEdit")
        .addAddButton(action -> insertItems(ingredientsInput(frame).showDialog(mealPlan)))
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
    deletedIngredientsStillInUse.forEach(ingredient -> {
      Optional<Ingredient> replaced = showReplaceDialog(frame, ingredients, ingredient);
      if (replaced.isPresent()) {
        mealPlan.replaceIngredient(ingredient, replaced.get());
      } else {
        ingredients.add(ingredient);
      }
    });
    mealPlan.setIngredients(ingredients);
    fileIoGui.savePart(mealPlan, INGREDIENTS);
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
        .sort((ingredient1, ingredient2) -> ingredient1.getName().compareTo(ingredient2.getName()));
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
