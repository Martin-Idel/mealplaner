package testcommonsmodel;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

public class HiddenIngredientFact implements IngredientFact, IngredientFactXml {
  private TestEnum testEnum;

  public HiddenIngredientFact() {
    this.testEnum = TestEnum.TEST1;
  }

  public HiddenIngredientFact(TestEnum testEnum) {
    this.testEnum = testEnum;
  }

  public TestEnum getTestEnum() {
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

  public enum TestEnum {
    TEST1, TEST2
  }
}
