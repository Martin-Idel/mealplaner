// SPDX-License-Identifier: MIT

package etoetests.nativeguitests;

import static etoetests.CommonFunctions.getMeal1;
import static etoetests.CommonFunctions.getRecipe2;
import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.RICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import etoetests.guitests.constants.ComponentNames;
import etoetests.guitests.helpers.MealplanerTestCase;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;

public class DatabaseEditChangeMealsTest extends MealplanerTestCase {
  public DatabaseEditChangeMealsTest() {
    super("src/test/resources/mealsXmlOnlyOneMeal.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void canChangeAllAspectsOfAMeal() throws Exception {
    Meal newMeal = from(getMeal1())
        .changeFact(new CourseTypeFact(DESERT))
        .changeFact(new CommentFact("New comment"))
        .recipe(getRecipe2())
        .create();
    List<Meal> meals = new ArrayList<>();
    meals.add(newMeal);

    windowHelpers.getMealsPane()
        .changeCookingTime(0, newMeal.getTypedMealFact(CookingTimeFact.class).getCookingTime())
        .changeCourseType(0, newMeal.getTypedMealFact(CourseTypeFact.class).getCourseType())
        .changeComment(0, "New comment")
        .enterRecipe(0, newMeal.getRecipe().get())
        .compareDatabaseInTable(meals);
  }

  @Test
  public void changingSavingNotSavingOfFieldsWorksCorrectly() throws Exception {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());

    windowHelpers.getMealsPane().mealsTabbedPane();
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_CANCEL);
    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_SAVE);

windowHelpers.getMealsPane()
    .changeCookingTime(0, MEDIUM)
    .changeSideDish(0, RICE)
    .changeComment(0, "New comment");
    clickButtonAndWaitForDisabledThenAssert(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_CANCEL);
    windowHelpers.getMealsPane().compareDatabaseInTable(meals);
    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_CANCEL);
    assertButtonIsDisabled(mainFrame, ComponentNames.BUTTON_DATABASEEDIT_SAVE);
  }
}
