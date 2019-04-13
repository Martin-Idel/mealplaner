// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.recepies;

import static java.util.stream.Collectors.toList;
import static mealplaner.model.recipes.QuantitativeIngredient.create;

import java.util.Comparator;
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
    this.ingredients = recipe.getIngredientsFor(recipe.getNumberOfPortions()).entrySet()
        .stream()
        .map(entry -> create(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparing(ingredient -> ingredient.getIngredient().getName()))
        .collect(toList());
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
