// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.ingredientsedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.FlexibleTableBuilder.createNewTable;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.recipes.IngredientBuilder.from;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientBuilder;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;
import mealplaner.model.recipes.Measures;
import mealplaner.plugins.api.IngredientEditExtension;

final class IngredientsEditTable {
  private IngredientsEditTable() {
  }

  public static Table createTable(
      List<Ingredient> ingredients,
      ButtonPanelEnabling buttonPanel,
      Function<Measures, Map<Measure, NonnegativeFraction>> measuresEdit,
      Collection<IngredientEditExtension> ingredientEditExtensions) {
    var tableModelBuilder = createNewTable()
        .withRowCount(ingredients::size)
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("insertIngredientName"))
            .getValueFromOrderedList(ingredients, Ingredient::getName)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newName) -> from(oldIngredient).withName(newName).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .buildWithOrderNumber(1))
        .addColumn(withEnumContent(IngredientType.class)
            .withColumnName(BUNDLES.message("insertTypeLength"))
            .getValueFromOrderedList(ingredients, Ingredient::getType)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newType) -> from(oldIngredient).withType(newType).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .buildWithOrderNumber(10))
        .addColumn(withEnumContent(Measure.class)
            .withColumnName(BUNDLES.message("insertMeasure"))
            .getValueFromOrderedList(ingredients, Ingredient::getPrimaryMeasure)
            .setValueToOrderedImmutableList(ingredients,
                (oldIngredient, newMeasure) -> from(oldIngredient).withPrimaryMeasure(newMeasure).create())
            .isEditable()
            .onChange(buttonPanel::enableButtons)
            .buildWithOrderNumber(70))
        .addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("insertSecondaryMeasures"))
            .getRowValueFromUnderlyingModel(
                row -> ingredients.get(row).getMeasures().getSecondaries().isEmpty()
                    ? BUNDLES.message("insertSecondaryMeasuresButtonDefaultLabel")
                    : BUNDLES.message("insertSecondaryMeasuresButtonLabel"))
            .buildWithOrderNumber(80))
        .addListenerToThisColumn(row -> {
          var measures = ingredients.get(row).getMeasures();
          Map<Measure, NonnegativeFraction> editedMeasures = measuresEdit.apply(measures);
          Ingredient newIngredient = IngredientBuilder.from(ingredients.get(row))
              .withSecondaryMeasures(editedMeasures)
              .create();
          ingredients.set(row, newIngredient);
          buttonPanel.enableButtons();
        });
    for (var ingredientEditExtension : ingredientEditExtensions) {
      ingredientEditExtension.addTableColumns(tableModelBuilder, ingredients);
    }
    return tableModelBuilder.buildTable();
  }

}
