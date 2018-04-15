package guitests;

import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.commons.Pair.of;
import static mealplaner.model.shoppinglist.ShoppingList.from;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class ProposalTest extends AssertJMealplanerTestCase {
  public ProposalTest() {
    super("src/test/resources/mealsXmlV2.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void saveDefaultSettings() {
    Settings defaultSettingTuesday = getSettings1();
    DayOfWeek dayTuesday = TUESDAY;
    Settings defaultSettingWednesday = getSettings2();
    DayOfWeek dayWednesday = WEDNESDAY;
    Map<DayOfWeek, Settings> defaultSettingsMap = new HashMap<>();
    defaultSettingsMap.put(dayTuesday, defaultSettingTuesday);
    defaultSettingsMap.put(dayWednesday, defaultSettingWednesday);
    DefaultSettings defaultSettings = DefaultSettings.from(defaultSettingsMap);

    windowHelpers.getProposalPane()
        .enterDefaultSettings(defaultSettings)
        .compareDefaultSettings(defaultSettings);
  }

  @Test
  public void makeProposalShowsCorrectOutput() {
    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();
    Meal meal3 = getMeal3();

    List<Meal> mealOutput = new ArrayList<>();
    mealOutput.add(meal3);
    mealOutput.add(meal1);
    mealOutput.add(meal2);

    List<Pair<Recipe, NonnegativeInteger>> recipeList = new ArrayList<>();
    recipeList.add(of(meal2.getRecipe().get(), nonNegative(2)));
    recipeList.add(of(meal3.getRecipe().get(), nonNegative(4)));

    windowHelpers.getProposalPane()
        .updateCookedLast()
        .enterNumberOfDaysForProposal(nonNegative(3))
        .proposeWithSettings(getSettings2())
        .requireProposalFrom(mealOutput)
        .assertMissingRecipes()
        .requireShoppingListContentAndClose(from(recipeList));
  }
}
