package mealplaner.plugins.plugins.courses.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withEnumContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;
import mealplaner.plugins.builtins.courses.CourseType;
import mealplaner.plugins.builtins.courses.CourseTypeFact;

public class MealEditCourseType implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withEnumContent(CourseType.class)
        .withColumnName(BUNDLES.message("courseTypeColumn"))
        .setValueToOrderedImmutableList(meals,
            (meal, courseType) -> from(meal).courseType(courseType).create())
        .getValueFromOrderedList(meals, meal -> meal.getTypedMealFact(CourseTypeFact.class).getCourseType())
        .isEditable()
        .onChange(buttonPanelEnabling::enableButtons)
        .buildWithOrderNumber(70));
  }
}
