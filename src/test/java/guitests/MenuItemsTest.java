package guitests;

import static java.util.Optional.of;
import static java.util.UUID.randomUUID;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.recipes.model.Ingredient.ingredient;
import static mealplaner.recipes.model.IngredientType.SPICE;
import static mealplaner.recipes.model.Measure.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;
import static testcommons.CommonFunctions.getRecipe1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.Meal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.CourseType;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.recipes.model.Ingredient;
import mealplaner.xml.IngredientsReader;

public class MenuItemsTest extends AssertJMealplanerTestCase {

  @Test
  public void createMenu() {
    Meal meal1 = createMeal(randomUUID(), "Bla", CookingTime.LONG, Sidedish.PASTA,
        ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, CourseType.ENTRY, nonNegative(2),
        "No comment", of(getRecipe1()));
    windowHelpers.enterMealFromMenu(meal1);
    List<Meal> meals = new ArrayList<>();
    meals.add(meal1);
    windowHelpers.compareDatabaseInTable(meals);
    window.close();
  }

  @Test
  public void createIngredient() throws IOException {
    Ingredient ingredient = ingredient("Test4", SPICE, NONE);

    window.menuItem("MenuFile").click();
    window.menuItem("MenuItemInsertIngredients").click();
    DialogFixture enterIngredientDialog = window.dialog();
    enterIngredientDialog.textBox("InputFieldNonemptyTextIngredientName")
        .setText(ingredient.getName());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientType")
        .selectItem(ingredient.getType().toString());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientMeasure")
        .selectItem(ingredient.getMeasure().toString());
    enterIngredientDialog.button("ButtonPanelIngredientsInput0").click();
    enterIngredientDialog.button("ButtonPanelIngredientsInput1").click();

    List<Ingredient> ingredientsAfterSaving = IngredientsReader
        .loadXml(DESTINATION_INGREDIENT_FILE_PATH);

    assertThat(ingredientsAfterSaving).hasSize(4);
    assertThat(ingredientsAfterSaving).contains(getIngredient1(),
        getIngredient2(),
        getIngredient3());
    // UUID is random upon creation, so we can't compare ingredients directly
    assertThat(ingredientsAfterSaving.get(3).getName()).isEqualTo(ingredient.getName());
    assertThat(ingredientsAfterSaving.get(3).getType()).isEqualTo(ingredient.getType());
    assertThat(ingredientsAfterSaving.get(3).getMeasure()).isEqualTo(ingredient.getMeasure());
  }
}
