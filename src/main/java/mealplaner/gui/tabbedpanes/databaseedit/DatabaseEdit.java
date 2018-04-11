package mealplaner.gui.tabbedpanes.databaseedit;

import static java.util.stream.Collectors.toList;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.Utils.not;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.gui.dialogs.mealinput.MealInput.mealinput;
import static mealplaner.model.meal.MealBuilder.from;

import java.awt.BorderLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.editing.NonemptyTextCellEditor;
import mealplaner.commons.gui.tables.Table;
import mealplaner.gui.dialogs.recepies.RecipeInput;
import mealplaner.io.FileIoGui;
import mealplaner.model.DataStore;
import mealplaner.model.DataStoreEventType;
import mealplaner.model.DataStoreListener;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.recipes.Recipe;

public class DatabaseEdit implements DataStoreListener {
  private final JFrame dataFrame;
  private final JPanel dataPanel;
  private Table table;

  private ButtonPanelEnabling buttonPanel;
  private final FileIoGui fileIoGui;

  private final DataStore mealplanerData;
  private final List<Meal> meals;

  public DatabaseEdit(DataStore mealPlan, JFrame parentFrame, JPanel parentPanel,
      FileIoGui fileIoGui) {
    this.mealplanerData = mealPlan;
    this.fileIoGui = fileIoGui;
    this.meals = new ArrayList<>(mealPlan.getMeals());
    mealPlan.register(this);

    dataFrame = parentFrame;
    dataPanel = parentPanel;
    dataPanel.setLayout(new BorderLayout());
  }

  public void setupPane(Consumer<List<Meal>> setMeals) {
    buttonPanel = createButtonPanelWithEnabling(setMeals);
    buttonPanel.disableButtons();

    table = createTable();

    dataPanel.add(table.getComponent(), BorderLayout.CENTER);
    dataPanel.add(buttonPanel.getPanel(), BorderLayout.SOUTH);
  }

  private Table createTable() {
    return createNewTable()
        .withRowCount(meals::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("mealNameColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, name) -> from(meal).name(name).create())
            .getValueFromOrderedList(meals, meal -> meal.getName())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .overwriteTableCellEditor(new NonemptyTextCellEditor())
            .build())
        .addColumn(withEnumContent(CookingTime.class)
            .withColumnName(BUNDLES.message("cookingLengthColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, cookingTime) -> from(meal).cookingTime(cookingTime).create())
            .getValueFromOrderedList(meals, meal -> meal.getCookingTime())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(Sidedish.class)
            .withColumnName(BUNDLES.message("sidedishColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, sidedish) -> from(meal).sidedish(sidedish).create())
            .getValueFromOrderedList(meals, meal -> meal.getSidedish())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(ObligatoryUtensil.class)
            .withColumnName(BUNDLES.message("utensilColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, utensil) -> from(meal).obligatoryUtensil(utensil).create())
            .getValueFromOrderedList(meals, meal -> meal.getObligatoryUtensil())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withNonnegativeIntegerContent()
            .withColumnName(BUNDLES.message("cookedLastTimeColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, day) -> from(meal).daysPassed(day).create())
            .getValueFromOrderedList(meals, meal -> meal.getDaysPassed())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(CookingPreference.class)
            .withColumnName(BUNDLES.message("popularityColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, preference) -> from(meal).cookingPreference(preference)
                    .create())
            .getValueFromOrderedList(meals, meal -> meal.getCookingPreference())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withEnumContent(CourseType.class)
            .withColumnName(BUNDLES.message("courseTypeColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, courseType) -> from(meal).courseType(courseType).create())
            .getValueFromOrderedList(meals, meal -> meal.getCourseType())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("commentInsertColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, comment) -> from(meal).comment(comment).create())
            .getValueFromOrderedList(meals, meal -> meal.getComment())
            .isEditable()
            .onChange(() -> buttonPanel.enableButtons())
            .build())
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("recipeEditColum"))
            .getRowValueFromUnderlyingModel(
                row -> meals.get(row).getRecipe().isPresent()
                    ? BUNDLES.message("editRecipeButtonLabel")
                    : BUNDLES.message("createRecipeButtonLabel"))
            .build())
        .addListenerToThisColumn((row) -> {
          Optional<Recipe> recipe = meals.get(row).getRecipe();
          Optional<Recipe> editedRecipe = new RecipeInput(
              dataFrame, BUNDLES.message("recipeInputDialogTitle"))
                  .showDialog(recipe, mealplanerData);
          Meal newMeal = from(meals.get(row)).optionalRecipe(editedRecipe).create();
          meals.set(row, newMeal);
          buttonPanel.enableButtons();
        })
        .buildTable();
  }

  private ButtonPanelEnabling createButtonPanelWithEnabling(Consumer<List<Meal>> setData) {
    return builder("DatabaseEdit")
        .addAddButton(action -> insertItem(mealinput(dataFrame).showDialog(mealplanerData)))
        .addRemoveSelectedButton(action -> {
          Arrays.stream(table.getSelectedRows())
              .collect(ArrayDeque<Integer>::new, ArrayDeque<Integer>::add,
                  ArrayDeque<Integer>::addAll)
              .descendingIterator()
              .forEachRemaining(number -> {
                meals.remove((int) number);
                table.deleteRows(number, number);
              });
          buttonPanel.enableButtons();
        })
        .addSaveButton(action -> {
          setData.accept(meals);
          fileIoGui.saveMeals(meals);
          buttonPanel.disableButtons();
        })
        .makeLastButtonEnabling()
        .addCancelButton(action -> updateTable())
        .makeLastButtonEnabling()
        .buildEnablingPanel();
  }

  public void printTable() {
    table.printTable(dataFrame);
  }

  public void insertItem(Optional<Meal> optionalMeal) {
    optionalMeal.ifPresent(meal -> addAtSortedPosition(meal));
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

  public void updateTable() {
    // We have to update the reference meals as we can't replace it - it's fixed to
    // the table
    List<Meal> newMeals = mealplanerData.getMeals();
    meals.removeAll(meals.stream().filter(not(newMeals::contains)).collect(toList()));
    newMeals.stream().filter(not(meals::contains)).map(Meal::copy).forEach(meals::add);
    meals.sort((meal1, meal2) -> meal1.compareTo(meal2));
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