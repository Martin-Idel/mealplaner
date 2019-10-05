package mealplaner.plugins.plugins.comment.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditComment implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withContent(String.class)
        .withColumnName(BUNDLES.message("commentInsertColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, comment) -> from(meal).comment(comment).create())
        .getValueFromOrderedList(meals, meal -> meal.getTypedMealFact(CommentFact.class).getComment())
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(80));
  }
}
