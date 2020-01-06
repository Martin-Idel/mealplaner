// SPDX-License-Identifier: MIT

package xmlcommons;

import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HiddenIngredientFact implements IngredientFact, IngredientFactXml {
  public enum HiddenEnum {
    TEST1, TEST2;
  }

  private final HiddenEnum testEnum;

  public HiddenIngredientFact() {
    this.testEnum = HiddenEnum.TEST1;
  }

  public HiddenIngredientFact(HiddenEnum testEnum) {
    this.testEnum = testEnum;
  }

  public HiddenEnum getTestEnum() {
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
    HiddenIngredientFact that = (HiddenIngredientFact) o;
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
