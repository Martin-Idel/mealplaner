package mealplaner.xml.model.v2;

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
  public int version;
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public final List<MealXml> meals;

  public MealdatabaseXml() {
    this(new ArrayList<>(), 2);
  }

  public MealdatabaseXml(List<MealXml> meals, int version) {
    this.meals = meals;
    this.version = version;
  }
}
