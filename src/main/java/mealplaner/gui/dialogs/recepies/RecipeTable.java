// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import java.util.List;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.tables.Table;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

class RecipeTable {
  private final List<Ingredient> ingredientList;
  private final List<QuantitativeIngredient> ingredients;

  public RecipeTable(Recipe recipe, List<Ingredient> ingredientList) {
    this.ingredientList = ingredientList;
    this.ingredients = recipe.getIngredientListFor(recipe.getNumberOfPortions());
  }

  public Table setupTable() {
    return IngredientsTable.setupTable(ingredients, ingredientList);
  }

  public Optional<Recipe> getRecipe(NonnegativeInteger numberOfPeople) {
    if (ingredients.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(Recipe.from(numberOfPeople, ingredients));
    }
  }
}
