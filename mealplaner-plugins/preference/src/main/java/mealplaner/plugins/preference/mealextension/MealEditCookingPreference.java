// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditCookingPreference implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withEnumContent(CookingPreference.class)
        .withColumnName(BUNDLES.message("popularityColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, preference) -> from(meal).addFact(new CookingPreferenceFact(preference))
                .create())
        .getValueFromOrderedList(
            meals, meal -> meal.getTypedMealFact(CookingPreferenceFact.class).getCookingPreference())
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(50));
  }
}
