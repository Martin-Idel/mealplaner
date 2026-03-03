// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withBooleanContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditChildPreference implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(
        withBooleanContent()
            .withColumnName(BUNDLES.message("childFriendlyColumn"))
            .setValueToOrderedImmutableList(meals,
                (meal, childFriendly) -> from(meal).changeFact(new ChildPreferenceFact(childFriendly)).create())
            .getValueFromOrderedList(meals,
                meal -> meal.getTypedMealFact(ChildPreferenceFact.class).isChildFriendly())
            .isEditable()
            .onChange(buttonPanelEnabling::enableButtons)
            .buildWithOrderNumber(65));
  }
}