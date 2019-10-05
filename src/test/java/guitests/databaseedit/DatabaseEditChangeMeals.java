// SPDX-License-Identifier: MIT

package guitests.databaseedit;

import static mealplaner.model.meal.MealBuilder.from;
import static mealplaner.model.meal.enums.CourseType.DESERT;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.plugins.sidedish.mealextension.Sidedish.RICE;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getRecipe2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import guitests.helpers.AssertJMealplanerTestCase;
import guitests.pageobjects.MealsEditPageObject;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.plugins.comment.mealextension.CommentFact;
import mealplaner.plugins.plugins.cookingtime.mealextension.CookingTimeFact;

public class DatabaseEditChangeMeals extends AssertJMealplanerTestCase {
  public DatabaseEditChangeMeals() {
    super("src/test/resources/mealsXmlOnlyOneMeal.xml",
        "src/test/resources/save.xml",
        "src/test/resources/ingredients.xml");
  }

  @Test
  public void canChangeAllAspectsOfAMeal() {
    Meal newMeal = from(getMeal1())
        .cookingTime(SHORT)
        .courseType(DESERT)
        .comment("New comment")
        .recipe(getRecipe2())
        .create();
    List<Meal> meals = new ArrayList<>();
    meals.add(newMeal);

    windowHelpers.getMealsPane()
        .changeCookingTime(0, newMeal.getTypedMealFact(CookingTimeFact.class).getCookingTime())
        .changeCourseType(0, newMeal.getCourseType())
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
