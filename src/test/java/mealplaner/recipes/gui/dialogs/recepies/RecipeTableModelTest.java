package mealplaner.recipes.gui.dialogs.recepies;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.BundleStore.BUNDLES;
import static mealplaner.recipes.model.Ingredient.emptyIngredient;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getIngredient1;
import static testcommons.CommonFunctions.getIngredient2;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;
import testcommons.BundlesInitialization;

public class RecipeTableModelTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	private RecipeTableModel recipeTableModel;

	@Before
	public void setUp() {
		ResourceBundle messages = new ResourceBundle() {
			@Override
			protected Object handleGetObject(String key) {
				return "fake_translated_value";
			}

			@Override
			public Enumeration<String> getKeys() {
				return Collections.emptyEnumeration();
			}
		};
		BUNDLES.setMessageBundle(messages);
	}

	@Test
	public void emptyTableWorksCorrectly() {
		recipeTableModel = new RecipeTableModel(empty());

		assertThat(recipeTableModel.getRowCount()).isEqualTo(1);
		assertThat(recipeTableModel.getValueAt(0, 0)).isEqualTo(emptyIngredient());
		assertThat(recipeTableModel.getValueAt(0, 1)).isEqualTo(0);
		assertThat(recipeTableModel.getValueAt(0, 2)).isEqualTo(Measure.NONE);
	}

	@Test
	public void gettingIngredientsFromKnownRecipeWorksCorrectly() {
		Map<Ingredient, Integer> recipeMap = new HashMap<>();
		recipeMap.put(getIngredient1(), 100);
		recipeMap.put(getIngredient2(), 50);
		Recipe recipe = Recipe.from(1, recipeMap);

		recipeTableModel = new RecipeTableModel(of(recipe));

		assertThat(recipeTableModel.getValueAt(1, 0)).isEqualTo(getIngredient1());
		assertThat(recipeTableModel.getValueAt(0, 0)).isEqualTo(getIngredient2());
		assertThat(recipeTableModel.getValueAt(2, 0)).isEqualTo(emptyIngredient());
	}

	@Test
	public void gettingMeasuresFromKnownRecipeWorksCorrectly() {
		Map<Ingredient, Integer> recipeMap = new HashMap<>();
		recipeMap.put(getIngredient1(), 100);
		recipeMap.put(getIngredient2(), 50);
		Recipe recipe = Recipe.from(1, recipeMap);

		recipeTableModel = new RecipeTableModel(of(recipe));

		assertThat(recipeTableModel.getValueAt(1, 2)).isEqualTo(getIngredient1().getMeasure());
		assertThat(recipeTableModel.getValueAt(0, 2)).isEqualTo(getIngredient2().getMeasure());
		assertThat(recipeTableModel.getValueAt(2, 2)).isEqualTo(emptyIngredient().getMeasure());
	}

	@Test
	public void gettingAmountsFromKnownRecipeWorksCorrectly() {
		Map<Ingredient, Integer> recipeMap = new HashMap<>();
		recipeMap.put(getIngredient1(), 50);
		recipeMap.put(getIngredient2(), 100);
		Recipe recipe = Recipe.from(1, recipeMap);

		recipeTableModel = new RecipeTableModel(of(recipe));

		assertThat(recipeTableModel.getValueAt(0, 1)).isEqualTo(100);
		assertThat(recipeTableModel.getValueAt(1, 1)).isEqualTo(50);
		assertThat(recipeTableModel.getValueAt(2, 1)).isEqualTo(0);
	}

	@Test
	public void settingAnIngredientIntoEmptyTableSetsAnIngredient() {
		recipeTableModel = new RecipeTableModel(empty());
		Ingredient ingredient1 = getIngredient1();

		recipeTableModel.setValueAt(ingredient1, 0, 0);

		assertThat(recipeTableModel.getValueAt(0, 0)).isEqualTo(ingredient1);
	}

	@Test
	public void settingAnIngredientIntoEmptyTableUpdatesMeasure() {
		recipeTableModel = new RecipeTableModel(empty());
		Ingredient ingredient1 = getIngredient1();

		recipeTableModel.setValueAt(ingredient1, 0, 0);

		assertThat(recipeTableModel.getValueAt(0, 2)).isEqualTo(ingredient1.getMeasure());
	}

	@Test
	public void settingAnIngredientIntoEmptyTableAddsRow() {
		recipeTableModel = new RecipeTableModel(empty());
		Ingredient ingredient1 = getIngredient1();

		recipeTableModel.setValueAt(ingredient1, 0, 0);

		assertThat(recipeTableModel.getRowCount()).isEqualTo(2);
		assertThat(recipeTableModel.getValueAt(1, 0)).isEqualTo(emptyIngredient());
		assertThat(recipeTableModel.getValueAt(1, 1)).isEqualTo(0);
		assertThat(recipeTableModel.getValueAt(1, 2)).isEqualTo(Measure.NONE);
	}

	@Test
	public void settingAnAmountCorrectlyUpdatesAmount() {
		recipeTableModel = new RecipeTableModel(empty());
		Ingredient ingredient1 = getIngredient1();
		recipeTableModel.setValueAt(ingredient1, 0, 0);

		int expectedAmount = 50;
		recipeTableModel.setValueAt(Integer.toString(expectedAmount), 0, 1);

		assertThat(recipeTableModel.getValueAt(0, 1)).isEqualTo(expectedAmount);
		assertThat(recipeTableModel.getRowCount()).isEqualTo(2);
		assertThat(recipeTableModel.getValueAt(1, 0)).isEqualTo(emptyIngredient());
		assertThat(recipeTableModel.getValueAt(1, 1)).isEqualTo(0);
		assertThat(recipeTableModel.getValueAt(1, 2)).isEqualTo(Measure.NONE);
	}

	@Test
	public void settingAnIngredientToEmptyDeletesARow() {
		recipeTableModel = new RecipeTableModel(empty());
		recipeTableModel.setValueAt(getIngredient1(), 0, 0);
		recipeTableModel.setValueAt(getIngredient2(), 1, 0);

		recipeTableModel.setValueAt(emptyIngredient(), 0, 0);

		assertThat(recipeTableModel.getRowCount()).isEqualTo(2);
		assertThat(recipeTableModel.getValueAt(1, 0)).isEqualTo(emptyIngredient());
		assertThat(recipeTableModel.getValueAt(1, 1)).isEqualTo(0);
		assertThat(recipeTableModel.getValueAt(1, 2)).isEqualTo(Measure.NONE);
	}

	@Test
	public void settingAnIngredientToSomethingThatAlreadyExistsDoesntChangeAnything() {
		recipeTableModel = new RecipeTableModel(empty());
		recipeTableModel.setValueAt(getIngredient1(), 0, 0);
		recipeTableModel.setValueAt(getIngredient2(), 1, 0);

		recipeTableModel.setValueAt(getIngredient1(), 1, 0);

		assertThat(recipeTableModel.getRowCount()).isEqualTo(3);
		assertThat(recipeTableModel.getValueAt(2, 0)).isEqualTo(emptyIngredient());
	}

	@Test
	public void settingAnIngredientToItselfDoesntChangeAnything() {
		recipeTableModel = new RecipeTableModel(empty());
		recipeTableModel.setValueAt(getIngredient1(), 0, 0);
		recipeTableModel.setValueAt(getIngredient2(), 1, 0);

		recipeTableModel.setValueAt(getIngredient1(), 1, 0);

		assertThat(recipeTableModel.getRowCount()).isEqualTo(3);
		assertThat(recipeTableModel.getValueAt(2, 0)).isEqualTo(emptyIngredient());
	}

	@Test
	public void gettingTheModelForEmptyTableReturnEmptyOptional() {
		recipeTableModel = new RecipeTableModel(empty());

		Optional<Recipe> recipe = recipeTableModel.getRecipe(1);

		assertThat(recipe.isPresent()).isFalse();
	}

	@Test
	public void gettingTheModelWorksCorrectly() {
		Recipe expectedRecipe = returnRecipeSetInTable();

		Optional<Recipe> recipe = recipeTableModel.getRecipe(1);

		assertThat(recipe.get()).isEqualTo(expectedRecipe);
	}

	@Test
	public void gettingTheModelWorksCorrectlyAfterChangingIngredientThatIsAlreadyPresent() {
		Recipe expectedRecipe = returnRecipeSetInTable();
		recipeTableModel.setValueAt(getIngredient1(), 1, 0);

		Optional<Recipe> recipe = recipeTableModel.getRecipe(1);

		assertThat(recipe.get()).isEqualTo(expectedRecipe);
	}

	@Test
	public void gettingTheModelWorksCorrectlyAfterSettingAnIngredientToItself() {
		Recipe expectedRecipe = returnRecipeSetInTable();
		recipeTableModel.setValueAt(getIngredient1(), 0, 0);

		Optional<Recipe> recipe = recipeTableModel.getRecipe(1);

		assertThat(recipe.get()).isEqualTo(expectedRecipe);
	}

	private Recipe returnRecipeSetInTable() {
		recipeTableModel = new RecipeTableModel(empty());
		recipeTableModel.setValueAt(getIngredient1(), 0, 0);
		recipeTableModel.setValueAt(getIngredient2(), 1, 0);
		recipeTableModel.setValueAt(Integer.toString(50), 0, 1);
		recipeTableModel.setValueAt(Integer.toString(100), 1, 1);
		Map<Ingredient, Integer> recipeMap = new HashMap<>();
		recipeMap.put(getIngredient1(), 100);
		recipeMap.put(getIngredient2(), 50);
		Recipe expectedRecipe = Recipe.from(1, recipeMap);
		return expectedRecipe;
	}
}
