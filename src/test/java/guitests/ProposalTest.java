package guitests;

import static guitests.helpers.TabbedPanes.PROPOSAL_SUMMARY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.LocalDate.now;
import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.SHORT;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.commons.Pair.of;
import static mealplaner.model.shoppinglist.ShoppingList.from;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getMeal3;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.Pair;
import mealplaner.model.meal.Meal;
import mealplaner.model.recipes.QuantitativeIngredient;
import mealplaner.model.recipes.Recipe;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.shoppinglist.ShoppingList;

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

    windowHelpers.enterDefaultSettings(defaultSettings);

    windowHelpers.compareDefaultSettings(defaultSettings);
  }

  @Test
  public void makeProposalShowsCorrectOutput() {
    Meal meal1 = getMeal1();
    Meal meal2 = getMeal2();
    Meal meal3 = getMeal3();

    window.tabbedPane().selectTab(PROPOSAL_SUMMARY.number());
    window.button("ButtonProposalSummaryUpdate").click();
    window.dialog().button("ButtonPanelUpdatePastMeals0").click();

    window.textBox("InputFieldNonnegativeIntegerNumberDays").enterText("3");

    window.button("ButtonProposalSummaryMakeProposal").click();
    windowHelpers.enterSetting(window.dialog().table(), getSettings2(), 0);
    window.dialog().button("ButtonPanelProposalSettingsInput2").click();

    List<Meal> mealOutput = new ArrayList<>();
    mealOutput.add(meal3);
    mealOutput.add(meal1);
    mealOutput.add(meal2);

    window.dialog().table().requireContents(generateProposalOutputContent(mealOutput));
    window.dialog().button("ButtonPanelProposalOutput1").click();

    window.optionPane().yesButton().click(); // Not all recipes have a recipe

    List<Pair<Recipe, NonnegativeInteger>> recipeList = new ArrayList<>();
    recipeList.add(of(meal2.getRecipe().get(), nonNegative(2)));
    recipeList.add(of(meal3.getRecipe().get(), nonNegative(4)));

    window.dialog().table().requireContents(generateShoppingListContent(from(recipeList)));
    window.dialog().button("ButtonPanelShoppingListDialog1").click();
  }

  private String[][] generateShoppingListContent(ShoppingList shoppingList) {
    List<QuantitativeIngredient> shoppings = shoppingList.getList();
    String[][] contents = new String[shoppings.size() + 1][3];
    for (int row = 0; row < shoppings.size(); row++) {
      QuantitativeIngredient ingredient = shoppings.get(row);
      contents[row][0] = ingredient.getIngredient().getName();
      contents[row][1] = ingredient.getAmount().toString();
      contents[row][2] = ingredient.getIngredient().getMeasure().toString();
    }
    contents[shoppings.size()][0] = "";
    contents[shoppings.size()][1] = "0";
    contents[shoppings.size()][2] = "-";
    return contents;
  }

  private String[][] generateProposalOutputContent(List<Meal> mealOutput) {
    String[][] contents = new String[mealOutput.size()][3];
    LocalDate date = now().plusDays(1);
    for (int row = 0; row < mealOutput.size(); row++) {
      contents[row][0] = ofLocalizedDate(SHORT).withLocale(BUNDLES.locale()).format(date);
      contents[row][1] = date.getDayOfWeek().getDisplayName(FULL, BUNDLES.locale());
      contents[row][2] = mealOutput.get(row).getName();
      date = date.plusDays(1);
    }
    return contents;
  }
}
