// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.tables.TableColumnBuilder.withContent;
import static mealplaner.model.meal.MealBuilder.from;

import java.util.List;

import mealplaner.commons.gui.buttonpanel.ButtonPanelEnabling;
import mealplaner.commons.gui.tables.FlexibleTableBuilder;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.MealEditExtension;

public class MealEditRecipePhoto implements MealEditExtension {
  @Override
  public FlexibleTableBuilder addTableColumns(
      FlexibleTableBuilder table, List<Meal> meals, ButtonPanelEnabling buttonPanelEnabling) {
    return table.addColumn(withContent(String.class)
            .withColumnName(BUNDLES.message("recipePhotoEditColum"))
            .getRowValueFromUnderlyingModel(
                row -> meals.get(row).getTypedMealFact(RecipePhotoFact.class).getPath().isPresent()
                    ? BUNDLES.message("editRecipePhotoButtonLabel")
                    : BUNDLES.message("createRecipePhotoButtonLabel"))
            .buildWithOrderNumber(90))
        .addListenerToThisColumn(row -> {
          RecipePhotoFact recipePhoto = meals.get(row).getTypedMealFact(RecipePhotoFact.class);
          RecipePhotoFact editedPhoto = changeRecipePhoto(recipePhoto);
          if (editedPhoto.getPath().isPresent()) {
            Meal newMeal = from(meals.get(row)).changeFact(editedPhoto).create();
            meals.set(row, newMeal);
            buttonPanelEnabling.enableButtons();
          }
        });
  }

  public RecipePhotoFact changeRecipePhoto(RecipePhotoFact recipePhoto) {
    var recipePhotoDialog = ShowPhotoDialog.recipePhotoInput(null);
    return recipePhotoDialog.showDialog(recipePhoto);
  }
}
