package mealplaner.gui.tabbedpanes.databaseedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withNonnegativeIntegerContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.editing.NonemptyTextCellEditor;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.CookingPreference;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.meal.enums.CourseType;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.recipes.Recipe;

public final class DatabaseEditTable {
  private DatabaseEditTable() {
  }

  public static Table createTable(ButtonPanelEnabling buttonPanel,
      List<Meal> meals,
      Function<Optional<Recipe>, Optional<Recipe>> editRecipe) {
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
          Optional<Recipe> editedRecipe = editRecipe.apply(recipe);
          Meal newMeal = from(meals.get(row)).optionalRecipe(editedRecipe).create();
          meals.set(row, newMeal);
          buttonPanel.enableButtons();
        })
        .buildTable();
  }

}