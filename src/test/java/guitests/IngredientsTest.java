package guitests;

import static guitests.helpers.TabbedPanes.INGREDIENTS_EDIT;
import static mealplaner.model.recipes.Ingredient.ingredient;
import static mealplaner.model.recipes.IngredientType.SPICE;
import static mealplaner.model.recipes.Measure.NONE;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;
import static testcommons.CommonFunctions.getIngredient3;

import java.io.IOException;
import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.io.xml.IngredientsReader;
import mealplaner.model.recipes.Ingredient;

// TODO Need more tests as in database edit
public class IngredientsTest extends AssertJMealplanerTestCase {

  @Test
  public void createIngredient() throws IOException {
    Ingredient ingredient = ingredient("Test4", SPICE, NONE);

    window.tabbedPane().selectTab(INGREDIENTS_EDIT.number()).click();
    window.button("ButtonPanelIngredientsEdit0").click();

    DialogFixture enterIngredientDialog = window.dialog();
    enterIngredientDialog.textBox("InputFieldNonemptyTextIngredientName")
        .setText(ingredient.getName());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientType")
        .selectItem(ingredient.getType().toString());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientMeasure")
        .selectItem(ingredient.getMeasure().toString());
    enterIngredientDialog.button("ButtonPanelIngredientsInput0").click();
    enterIngredientDialog.button("ButtonPanelIngredientsInput1").click();

    window.button("ButtonPanelIngredientsEdit2").click();

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
