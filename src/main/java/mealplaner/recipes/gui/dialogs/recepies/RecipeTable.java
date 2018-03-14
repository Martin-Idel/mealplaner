package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.stream.Collectors.toList;
import static mealplaner.recipes.model.QuantitativeIngredient.create;

import java.util.List;
import java.util.Optional;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.tables.Table;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.QuantitativeIngredient;
import mealplaner.recipes.model.Recipe;

public class RecipeTable {
  private final List<Ingredient> ingredientList;
  private final List<QuantitativeIngredient> ingredients;

  public RecipeTable(Recipe recipe, List<Ingredient> ingredientList) {
    this.ingredientList = ingredientList;
    this.ingredients = recipe.getIngredientsFor(recipe.getNumberOfPortions()).entrySet()
        .stream()
        .map(entry -> create(entry.getKey(), entry.getValue()))
        .sorted((ingredient1, ingredient2) -> ingredient1.getIngredient().getName()
            .compareTo(ingredient2.getIngredient().getName()))
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
