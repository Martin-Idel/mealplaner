// SPDX-License-Identifier: MIT

package etoetests.guitests.databaseedit;

import static etoetests.CommonFunctions.getMeal1;
import static etoetests.CommonFunctions.getRecipe2;
import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.plugins.builtins.courses.CourseType.DESERT;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.sidedish.mealextension.Sidedish.RICE;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import etoetests.guitests.helpers.AssertJMealplanerTestCase;
import etoetests.guitests.pageobjects.MealsEditPageObject;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.builtins.courses.CourseTypeFact;
import mealplaner.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.cookingtime.mealextension.CookingTimeFact;

public class DatabaseEditChangeMealsTest extends AssertJMealplanerTestCase {
  public DatabaseEditChangeMealsTest() {
    super("src/test/resources/mealsXmlOnlyOneMeal.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void canChangeAllAspectsOfAMeal() {
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
        .changeComment(0, newMeal.getTypedMealFact(CommentFact.class).getComment())
        .enterRecipe(0, newMeal.getRecipe().get())
        .compareDatabaseInTable(meals);
  }

  @Test
  public void changingSavingNotSavingOfFieldsWorksCorrectly() {
    List<Meal> meals = new ArrayList<>();
    meals.add(getMeal1());

    MealsEditPageObject mealsPane = windowHelpers.getMealsPane().mealsTabbedPane();
    mealsPane.cancelButton().click();
    mealsPane.cancelButton().requireDisabled();
    mealsPane.saveButton().requireDisabled();
    mealsPane.changeCookingTime(0, MEDIUM)
        .clickCancelButtonMakingSureItIsEnabled()
        .changeSideDish(0, RICE)
        .clickCancelButtonMakingSureItIsEnabled()
        .changeComment(0, "New comment")
        .clickCancelButtonMakingSureItIsEnabled()
        .compareDatabaseInTable(meals);
  }
}
