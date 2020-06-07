// SPDX-License-Identifier: MIT

package mealplaner.plugins.vegetarian.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditVegetarian implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(
        withBooleanContent()
            .withColumnName(BUNDLES.message("vegetarianColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, veggie) -> from(meal).changeFact(new VegetarianFact(veggie)).create())
            .getValueFromOrderedList(meals,
                meal -> meal.getTypedMealFact(VegetarianFact.class).isVegetarian())
            .isEditable()
            .onChange(buttonPanelEnabling::enableButtons)
            .buildWithOrderNumber(65));
  }
}
