// SPDX-License-Identifier: MIT

package xmlcommons;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TestMealFact implements MealFact, MealFactXml {
  public enum TestEnum {
    TEST1, TEST2;
  }

  private final TestEnum testEnum;

  public TestMealFact() {
    this.testEnum = TestEnum.TEST1;
  }

  public TestMealFact(TestEnum testEnum) {
    this.testEnum = testEnum;
  }

  public TestEnum getTestEnum() {
    return testEnum;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TestMealFact that = (TestMealFact) o;
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

