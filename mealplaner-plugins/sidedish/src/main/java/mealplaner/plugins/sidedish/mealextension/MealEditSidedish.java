package mealplaner.plugins.sidedish.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditSidedish implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withEnumContent(Sidedish.class)
        .withColumnName(BUNDLES.message("sidedishColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, sidedish) -> from(meal).changeFact(new SidedishFact(sidedish)).create())
        .getValueFromOrderedList(meals, meal -> meal.getTypedMealFact(SidedishFact.class).getSidedish())
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(20));
  }
}
