package testcommons;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.model.Proposal.from;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.Settings.from;
import static mealplaner.recipes.model.Ingredient.ingredient;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import mealplaner.commons.BundleStore;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
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

public final class CommonFunctions {
  private CommonFunctions() {
  }

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
        of(getRecipe1()));
  }

  public static Meal getMeal3() {
    return createMeal("Test3", CookingTime.MEDIUM, Sidedish.RICE,
        ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, nonNegative(2), "",
        of(getRecipe2()));
  }

  public static Recipe getRecipe1() {
    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), nonNegative(100));
    ingredients.put(getIngredient2(), nonNegative(200));
    return Recipe.from(nonNegative(2), ingredients);
  }

  public static Recipe getRecipe2() {
    Map<Ingredient, NonnegativeInteger> ingredients = new HashMap<>();
    ingredients.put(getIngredient1(), nonNegative(100));
    ingredients.put(getIngredient3(), nonNegative(400));
    return Recipe.from(nonNegative(4), ingredients);
  }

  public static Ingredient getIngredient1() {
    return ingredient("Test1", IngredientType.FRESH_FRUIT, Measure.GRAM);
  }

  public static Ingredient getIngredient2() {
    return ingredient("Test2", IngredientType.BAKING_GOODS, Measure.MILLILITRE);
  }

  public static Ingredient getIngredient3() {
    return ingredient("Test3", IngredientType.CANNED_FRUIT, Measure.GRAM);
  }

  public static Settings getSettings1() {
    return from(cookingTimeWithProhibited(CookingTime.VERY_SHORT), nonNegative(3),
        CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);
  }

  public static Settings getSettings2() {
    return from(cookingTimeWithProhibited(CookingTime.SHORT), nonNegative(4),
        CasseroleSettings.POSSIBLE, PreferenceSettings.RARE_PREFERED);
  }

  public static Proposal proposal1() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    meals.add(getMeal2());
    List<Settings> settings = new ArrayList<>();
    settings.add(getSettings1());
    settings.add(getSettings2());
    LocalDate date = LocalDate.of(2017, 7, 5);
    return from(true, meals, settings, date);
  }

  public static Proposal proposal2() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal2());
    meals.add(getMeal3());
    List<Settings> settings = new ArrayList<>();
    settings.add(getSettings1());
    settings.add(getSettings2());
    LocalDate date = LocalDate.of(2017, 7, 5);
    return from(true, meals, settings, date);
  }
}
