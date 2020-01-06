// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MealData")
@XmlAccessorType(XmlAccessType.FIELD)
public class MealdatabaseXml {
  private int version = 3;
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public final List<MealXml> meals;

  public MealdatabaseXml() {
    this(new ArrayList<>());
  }

  public MealdatabaseXml(List<MealXml> meals) {
    this.meals = meals;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
