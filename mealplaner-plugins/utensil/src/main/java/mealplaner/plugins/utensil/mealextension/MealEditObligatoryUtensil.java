// SPDX-License-Identifier: MIT

package mealplaner.plugins.utensil.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditObligatoryUtensil implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withEnumContent(ObligatoryUtensil.class)
        .withColumnName(BUNDLES.message("utensilColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, utensil) -> from(meal).addFact(new ObligatoryUtensilFact(utensil)).create())
        .getValueFromOrderedList(
            meals, meal -> meal.getTypedMealFact(ObligatoryUtensilFact.class).getObligatoryUtensil())
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(30));
  }
}
