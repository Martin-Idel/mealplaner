package mealplaner.io.xml;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestIngredientFact implements IngredientFact, IngredientFactXml {
  public enum TestEnum {
    TEST1, TEST2;
  }

  private final TestEnum testEnum;

  public TestIngredientFact() {
    this.testEnum = TestEnum.TEST1;
  }

  public TestIngredientFact(TestEnum testEnum) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestIngredientFact that = (TestIngredientFact) o;
    return testEnum == that.testEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(testEnum);
  }

  @Override
  public String toString() {
    return "[testEnum=" + testEnum + "]";
  }
}

