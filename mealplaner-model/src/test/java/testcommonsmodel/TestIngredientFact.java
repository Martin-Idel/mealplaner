package testcommonsmodel;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

public class TestIngredientFact implements IngredientFactXml, IngredientFact {
  private TestIngredientEnum testEnum;

  public TestIngredientFact() {
    this.testEnum = TestIngredientEnum.TEST_INGREDIENT_1;
  }

  public TestIngredientFact(TestIngredientEnum testEnum) {
    this.testEnum = testEnum;
  }

  public TestIngredientEnum getTestEnum() {
    return testEnum;
  }

  @Override
  public IngredientFactXml convertToXml() {
    return this;
  }

  @Override
  public IngredientFact convertToFact() {
    return this;
  }

  public enum TestIngredientEnum {
    TEST_INGREDIENT_1, TEST_INGREDIENT_2
  }
}
