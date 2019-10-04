package mealplaner.plugins.plugins.cookingtime.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditCookingTime implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanel) {
    return table.addColumn(withEnumContent(CookingTime.class)
        .withColumnName(BUNDLES.message("cookingLengthColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, cookingTime) -> from(meal).cookingTime(cookingTime).create())
        .getValueFromOrderedList(meals, meal -> meal.getTypedMealFact(CookingTimeFact.class).getCookingTime())
        .isEditable()
        .onChange(buttonPanel::enableButtons)
        .buildWithOrderNumber(10));
  }
}
