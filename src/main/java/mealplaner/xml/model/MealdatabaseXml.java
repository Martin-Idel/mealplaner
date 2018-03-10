package mealplaner.xml.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MealData")
public class MealdatabaseXml {
  public int version;
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public final List<MealXml> meals;

  public MealdatabaseXml() {
    this(new ArrayList<>(), 1);
  }

  public MealdatabaseXml(List<MealXml> meals, int version) {
    this.meals = meals;
    this.version = version;
  }
}
