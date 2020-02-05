// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.pageobjects.TabbedPanes.INGREDIENTS_EDIT;
import static mealplaner.commons.BundleStore.BUNDLES;
import static org.assertj.swing.data.TableCell.row;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientType;
import mealplaner.model.recipes.Measure;

public class IngredientsEditPageObject {
  private static final int NUMBER_OF_INGREDIENT_DATA_COLUMNS = 4;

  private static final int INGREDIENTS_NAME_COLUMN = 0;
  private static final int INGREDIENTS_TYPE_COLUMN = 1;
  private static final int INGREDIENTS_MEASURE_COLUMN = 2;

  private final FrameFixture window;

  IngredientsEditPageObject(FrameFixture window) {
    this.window = window;
  }

  public IngredientsEditPageObject compareIngredientsInTable(List<Ingredient> ingredients) {
    JTableFixture ingredientsTable = window.table()
        .requireColumnCount(NUMBER_OF_INGREDIENT_DATA_COLUMNS)
        .requireRowCount(ingredients.size());
    ingredientsTable.requireContents(ingredientsToTableContent(ingredients));
    return this;
  }

  public IngredientsEditPageObject addIngredient(Ingredient ingredient) {
    addIngredientButton().click();

    enterIngredientInDialog(ingredient);

    saveButton().click();
    return this;
  }

  public IngredientsEditPageObject removeIngredients(int... rows) {
    JTableFixture ingredientsTable = window.table()
        .requireColumnCount(NUMBER_OF_INGREDIENT_DATA_COLUMNS);
    ingredientsTable.selectRows(rows);

    removeSelectedButton().click();
    return this;
  }

  public IngredientsEditPageObject saveButDoNotRemoveUsedIngredients() {
    saveButton().click();
    window.optionPane().buttonWithText(BUNDLES.message("donotDeleteIngredientButton")).click();
    return this;
  }

  public IngredientsEditPageObject saveAndReplaceIngredientsWithDefaults() {
    saveButton().click();
    window.optionPane().buttonWithText(BUNDLES.message("replaceIngredientButton")).click();
    return this;
  }

  public IngredientsEditPageObject clickCancelButtonMakingSureItIsEnabled() {
    cancelButton().requireEnabled();
    saveButton().requireEnabled();
    cancelButton().click();
    cancelButton().requireDisabled();
    saveButton().requireDisabled();
    return this;
  }

  public IngredientsEditPageObject changeName(int row, String name) {
    JTableFixture table = window.table();
    table.cell(row(row).column(INGREDIENTS_NAME_COLUMN)).enterValue(name);
    return this;
  }

  public IngredientsEditPageObject changeType(int row, IngredientType type) {
    JTableFixture table = window.table();
    table.cell(row(row).column(INGREDIENTS_TYPE_COLUMN)).enterValue(type.toString());
    return this;
  }

  public IngredientsEditPageObject changeMeasure(int row, Measure measure) {
    JTableFixture table = window.table();
    table.cell(row(row).column(INGREDIENTS_MEASURE_COLUMN)).enterValue(measure.toString());
    return this;
  }

  private JButtonFixture addIngredientButton() {
    return window.button("ButtonPanelIngredientsEdit0");
  }

  private JButtonFixture removeSelectedButton() {
    return window.button("ButtonPanelIngredientsEdit1");
  }

  public JButtonFixture saveButton() {
    return window.button("ButtonPanelIngredientsEdit2");
  }

  private JButtonFixture cancelButton() {
    return window.button("ButtonPanelIngredientsEdit3");
  }

  public IngredientsEditPageObject ingredientsTabbedPane() {
    window.tabbedPane().selectTab(INGREDIENTS_EDIT.number()).click();
    return this;
  }

  private void enterIngredientInDialog(Ingredient ingredient) {
    DialogFixture enterIngredientDialog = window.dialog();
    enterIngredient(ingredient, enterIngredientDialog);
  }

  static void enterIngredient(Ingredient ingredient, DialogFixture enterIngredientDialog) {
    enterIngredientDialog.textBox("InputFieldNonemptyTextIngredientName")
        .setText(ingredient.getName());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientType")
        .selectItem(ingredient.getType().toString());
    enterIngredientDialog.comboBox("InputFieldComboBoxIngredientMeasure")
        .selectItem(ingredient.getPrimaryMeasure().toString());
    if (!ingredient.getMeasures().getSecondaries().isEmpty()) {
      enterIngredientDialog.button("InputFieldButtonIngredientMeasures").click();
      enterIngredientSecondaryMeasures(ingredient.getMeasures().getSecondaries(), enterIngredientDialog);
    }
    enterIngredientDialog.button("ButtonPanelIngredientsInput1").click();
  }

  private static void enterIngredientSecondaryMeasures(
      EnumMap<Measure, NonnegativeFraction> secondaries, DialogFixture enterIngredientDialog) {
    JTableFixture ingredientsTable = enterIngredientDialog.table();
    var secondaryList = new ArrayList<>(secondaries.entrySet());
    for (int i = 0; i < secondaries.size(); i++) {
      ingredientsTable.enterValue(row(i).column(0), secondaryList.get(i).getKey().toString());
      ingredientsTable.enterValue(row(i).column(1), secondaryList.get(i).getValue().toString());
    }
    enterIngredientDialog.button("ButtonPanelMeasuresInput2").click();
  }

  private String[][] ingredientsToTableContent(List<Ingredient> ingredients) {
    String[][] content = new String[ingredients.size()][NUMBER_OF_INGREDIENT_DATA_COLUMNS];
    for (int i = 0; i < ingredients.size(); i++) {
      Ingredient ingredient = ingredients.get(i);
      content[i][0] = ingredient.getName();
      content[i][1] = ingredient.getType().toString();
      content[i][2] = ingredient.getPrimaryMeasure().toString();
      content[i][3] = ingredient.getMeasures().getSecondaries().isEmpty()
          ? BUNDLES.message("insertSecondaryMeasuresButtonDefaultLabel")
          : BUNDLES.message("insertSecondaryMeasuresButtonLabel");
    }
    return content;
  }
}
