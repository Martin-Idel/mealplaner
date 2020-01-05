package mealplaner.model.meal;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HiddenMealFact implements MealFact, MealFactXml {
  public enum HiddenEnum {
    TEST1, TEST2;
  }

  private final HiddenEnum testEnum;

  public HiddenMealFact() {
    this.testEnum = HiddenEnum.TEST1;
  }

  public HiddenMealFact(HiddenEnum testEnum) {
    this.testEnum = testEnum;
  }

  public HiddenEnum getTestEnum() {
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
    HiddenMealFact that = (HiddenMealFact) o;
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
