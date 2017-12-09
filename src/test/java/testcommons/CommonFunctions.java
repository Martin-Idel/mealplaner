package testcommons;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.Settings.from;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import mealplaner.BundleStore;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.Meal;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.model.Ingredient;
import mealplaner.recipes.model.IngredientType;
import mealplaner.recipes.model.Measure;
import mealplaner.recipes.model.Recipe;

public class CommonFunctions {
	public static <T extends Enum<T>> void allEnumValuesHaveACorrespondingStringRepresentation(
			T[] enumValues,
			Function<BundleStore, EnumMap<T, String>> getEnumStrings) {
		BundleStore bundles = mock(BundleStore.class);
		when(bundles.message(anyString())).thenReturn("");

		EnumMap<T, String> enumMap = getEnumStrings.apply(bundles);
		for (T setting : enumValues) {
			assertTrue(enumMap.containsKey(setting));
		}
	}

	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	public static Meal getMeal1() {
		return createMeal("Test1", CookingTime.SHORT, Sidedish.PASTA,
				ObligatoryUtensil.PAN, CookingPreference.VERY_POPULAR, nonNegative(5), "no comment",
				empty());
	}

	public static Meal getMeal2() {
		return createMeal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, nonNegative(1), "",
				of(getRecipe()));
	}

	public static Recipe getRecipe() {
		Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
		ingredients.put(getIngredient1(), nonNegative(100));
		ingredients.put(getIngredient2(), nonNegative(200));
		return Recipe.from(nonNegative(2), ingredients);
	}

	public static Ingredient getIngredient1() {
		return new Ingredient("Test1", IngredientType.FRESH_FRUIT,
				Measure.GRAM);
	}

	public static Ingredient getIngredient2() {
		return new Ingredient("Test2", IngredientType.BAKING_GOODS,
				Measure.MILLILITRE);
	}

	public static Ingredient getIngredient3() {
		return new Ingredient("Test3", IngredientType.CANNED_FRUIT,
				Measure.GRAM);
	}

	public static Settings getSettings1() {
		return from(cookingTimeWithProhibited(CookingTime.VERY_SHORT), nonNegative(3),
				CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);
	}
}
