// SPDX-License-Identifier: MIT

package etoetests.nativeguitests.ingredients;

import static etoetests.CommonFunctions.getIngredient1;
import static etoetests.CommonFunctions.getIngredient2;
import static etoetests.CommonFunctions.getIngredient3;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.recipes.IngredientBuilder.ingredient;
import static mealplaner.model.recipes.IngredientType.FLUID;
import static mealplaner.model.recipes.Measure.TEASPOON;
import static mealplaner.model.recipes.Measures.createMeasures;
import static mealplaner.model.recipes.QuantitativeIngredient.createQuantitativeIngredient;
import static mealplaner.commons.NonnegativeFraction.wholeNumber;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.junit.jupiter.api.Test;

import etoetests.guitests.helpers.NonAssertJMealplanerTestCase;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientBuilder;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;

public class IngredientsEditDatabaseTestNative extends NonAssertJMealplanerTestCase {
  public IngredientsEditDatabaseTestNative() {
    super("src/test/resources/mealsXmlV3.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredientsXmlV3.xml");
  }

  @Test
  public void canChangeAllAspectOfAnIngredient() throws Exception {
    var newIngredient = ingredient()
        .withUuid(getIngredient1().getId())
        .withName("New name")
        .withType(FLUID)
        .withMeasures(createMeasures(TEASPOON))
        .create();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpersNative.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getPrimaryMeasure())
        .compareIngredientsInTable(ingredients);
  }

  @Test
  public void changingIngredientsAndCancellingWorksForAllAspects() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpersNative.getIngredientsPane()
        .changeName(0, "New name");
    Thread.sleep(100);
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().cancelButton().doClick();
    });
    Thread.sleep(500);

    windowHelpersNative.getIngredientsPane()
        .changeType(0, FLUID);
    Thread.sleep(100);
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().cancelButton().doClick();
    });
    Thread.sleep(500);

    windowHelpersNative.getIngredientsPane()
        .changeMeasure(0, TEASPOON);
    Thread.sleep(100);
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().cancelButton().doClick();
    });
    Thread.sleep(500);

    windowHelpersNative.getIngredientsPane()
        .compareIngredientsInTable(ingredients);
  }

  @Test
  public void removingIngredientReplacesIfIngredientInUse() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient3());

    windowHelpersNative.getIngredientsPane()
        .removeIngredients(1)
        .saveAndReplaceIngredientsWithDefaults()
        .compareIngredientsInTable(ingredients);
    
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().saveButton().doClick();
    });
    Thread.sleep(1000);
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(windowHelpersNative.getIngredientsPane().saveButton().isEnabled()).isFalse();
    });
  }

  @Test
  public void editingNameReplacesNameInRecipe() throws Exception {
    Ingredient newIngredient = IngredientBuilder.from(getIngredient1()).withName("New name").create();
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(newIngredient);
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpersNative.getIngredientsPane()
        .changeName(0, newIngredient.getName())
        .changeType(0, newIngredient.getType())
        .changeMeasure(0, newIngredient.getPrimaryMeasure())
        .compareIngredientsInTable(ingredients);
    
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().saveButton().doClick();
    });
    Thread.sleep(500);
  }

  @Test
  public void removingIngredientDoesNotRemoveOnRequestIfIngredientInUse() throws Exception {
    List<Ingredient> ingredients = new ArrayList<>();
    ingredients.add(getIngredient1());
    ingredients.add(getIngredient2());
    ingredients.add(getIngredient3());

    windowHelpersNative.getIngredientsPane()
        .removeIngredients(1)
        .saveButDoNotRemoveUsedIngredients();
    
    Thread.sleep(1500);
    
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      System.out.println("Save button enabled: " + windowHelpersNative.getIngredientsPane().saveButton().isEnabled());
    });
    
    windowHelpersNative.getIngredientsPane().compareIngredientsInTable(ingredients);
    
    javax.swing.SwingUtilities.invokeLater(() -> {
      windowHelpersNative.getIngredientsPane().saveButton().doClick();
    });
    Thread.sleep(500);
    
    javax.swing.SwingUtilities.invokeAndWait(() -> {
      assertThat(windowHelpersNative.getIngredientsPane().saveButton().isEnabled()).isFalse();
    });
  }
}