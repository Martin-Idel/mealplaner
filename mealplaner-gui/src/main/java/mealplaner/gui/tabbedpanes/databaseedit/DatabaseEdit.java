// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.databaseedit;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.BundleUtils.not;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.tables.TableHelpers.deleteSelectedRows;
import static mealplaner.gui.dialogs.mealinput.MealInput.mealinput;
import static mealplaner.gui.dialogs.recepies.RecipeInput.recipeInput;
import static mealplaner.gui.tabbedpanes.databaseedit.DatabaseEditTable.createTable;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.ioapi.DataParts;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.DataStoreEventType;
import mealplaner.model.DataStoreListener;
import mealplaner.model.MealplanerData;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.PluginStore;

public class DatabaseEdit implements DataStoreListener {
  private final JFrame dataFrame;
  private final JPanel dataPanel;
  private Table table;

  private ButtonPanelEnabling buttonPanel;
  private final FileIoInterface fileIo;

  private final MealplanerData mealplanerData;
  private final List<Meal> meals;

  public DatabaseEdit(MealplanerData mealPlan, JFrame parentFrame, JPanel parentPanel,
      FileIoInterface fileIo) {
    this.mealplanerData = mealPlan;
    this.fileIo = fileIo;
    this.meals = new ArrayList<>(mealPlan.getMeals());
    mealPlan.register(this);

    dataFrame = parentFrame;
    dataPanel = parentPanel;
    dataPanel.setLayout(new BorderLayout());
  }

  public void setupPane(Consumer<List<Meal>> setMeals, PluginStore pluginStore) {
    buttonPanel = createButtonPanelWithEnabling(setMeals, pluginStore);
    buttonPanel.disableButtons();

    table = createTable(buttonPanel,
        meals,
        recipe -> recipeInput(dataFrame).showDialog(recipe, mealplanerData, pluginStore),
        pluginStore.getRegisteredMealEditGuiExtensions());

    dataPanel.add(table.getComponent(), BorderLayout.CENTER);
    dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
  }

  private ButtonPanelEnabling createButtonPanelWithEnabling(Consumer<List<Meal>> setData, PluginStore store) {
    return builder("DatabaseEdit")
        .addAddButton(action -> insertItem(mealinput(dataFrame).showDialog(mealplanerData, store)))
        .addRemoveSelectedButton(
            action -> deleteSelectedRows(table, number -> meals.remove((int) number)))
        .addSaveButton(action -> {
          setData.accept(meals);
          fileIo.savePart(mealplanerData, DataParts.MEALS);
          buttonPanel.disableButtons();
        })
        .makeLastButtonEnabling()
        .addCancelButton(action -> updateTable())
        .makeLastButtonEnabling()
        .buildEnablingPanel();
  }

  private void insertItem(Optional<Meal> optionalMeal) {
    optionalMeal.ifPresent(this::addAtSortedPosition);
  }

  private void addAtSortedPosition(Meal meal) {
    int row = 0;
    while (row < meals.size() && meal.compareTo(meals.get(row)) >= 0) {
      row++;
    }
    meals.add(row, meal);
    table.insertRows(row, row);
    buttonPanel.enableButtons();
  }

  private void updateTable() {
    // We have to update the reference meals as it is not replaceable in the table
    List<Meal> newMeals = mealplanerData.getMeals();
    meals.removeAll(meals.stream().filter(not(newMeals::contains)).collect(toList()));
    newMeals.stream().filter(not(meals::contains)).map(Meal::copy).forEach(meals::add);
    meals.sort(Meal::compareTo);
    table.update();
    buttonPanel.disableButtons();
  }

  public List<Meal> getMeals() {
    return meals;
  }

  @Override
  public void updateData(DataStoreEventType event) {
    if (event == DataStoreEventType.DATABASE_EDITED) {
      updateTable();
    }
  }
}