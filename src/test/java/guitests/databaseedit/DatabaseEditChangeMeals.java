package guitests.databaseedit;

import static guitests.helpers.TabbedPanes.DATABASE_EDIT;
import static mealplaner.model.MealBuilder.from;
import static mealplaner.model.enums.CookingTime.MEDIUM;
import static mealplaner.model.enums.CookingTime.SHORT;
import static mealplaner.model.enums.CourseType.DESERT;
import static mealplaner.model.enums.Sidedish.RICE;
import static org.assertj.swing.data.TableCell.row;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getRecipe2;

import java.util.ArrayList;
import java.util.List;

import org.assertj.swing.fixture.JTableFixture;
import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import mealplaner.model.Meal;
import mealplaner.recipes.model.Recipe;

public class DatabaseEditChangeMeals extends AssertJMealplanerTestCase {
  public static final int DATABASE_TIME_COLUMN = 1;
  public static final int DATABASE_SIDEDISH_COLUMN = 2;
  public static final int DATABASE_COURSE_TYPE_COLUMN = 6;
  public static final int DATABASE_COMMENT_COLUMN = 7;
  public static final int DATABASE_RECIPE_COLUMN = 8;

  public DatabaseEditChangeMeals() {
    super("src/test/resources/mealsXmlOnlyOneMeal.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void canChangeAllAspectsOfAMeal() {
    Meal meal1 = getMeal1();
    Recipe recipe = getRecipe2();
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    JTableFixture table = window.table();
    table.cell(row(0).column(DATABASE_TIME_COLUMN)).enterValue(SHORT.toString());
    table.cell(row(0).column(DATABASE_COURSE_TYPE_COLUMN)).enterValue(DESERT.toString());
    table.cell(row(0).column(DATABASE_COMMENT_COLUMN)).enterValue("New comment");
    table.cell(row(0).column(DATABASE_RECIPE_COLUMN)).click();
    windowHelpers.enterRecipe(recipe, window.dialog());
    Meal newMeal = from(meal1)
        .cookingTime(SHORT)
        .courseType(DESERT)
        .comment("New comment")
        .recipe(recipe)
        .create();
    List<Meal> meals = new ArrayList<>();
    meals.add(newMeal);
    windowHelpers.compareDatabaseInTable(meals);
    window.close();
  }

  @Test
  public void changingSavingNotSavingOfFieldsWorksCorrectly() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());
    window.tabbedPane().selectTab(DATABASE_EDIT.number()).click();
    window.button("ButtonPanelDatabaseEdit2").click();
    window.button("ButtonPanelDatabaseEdit3").requireDisabled();
    window.button("ButtonPanelDatabaseEdit2").requireDisabled();

    JTableFixture table = window.table();
    table.cell(row(0).column(DATABASE_TIME_COLUMN)).enterValue(MEDIUM.toString());
    checkDisabilityAndAbort();
    table.cell(row(0).column(DATABASE_SIDEDISH_COLUMN)).enterValue(RICE.toString());
    checkDisabilityAndAbort();
    table.cell(row(0).column(DATABASE_COMMENT_COLUMN)).enterValue("New comment");
    checkDisabilityAndAbort();

    windowHelpers.compareDatabaseInTable(meals);
  }

  private void checkDisabilityAndAbort() {
    window.button("ButtonPanelDatabaseEdit3").requireEnabled();
    window.button("ButtonPanelDatabaseEdit2").requireEnabled();
    window.button("ButtonPanelDatabaseEdit3").click();
    window.button("ButtonPanelDatabaseEdit3").requireDisabled();
    window.button("ButtonPanelDatabaseEdit2").requireDisabled();
  }
}
