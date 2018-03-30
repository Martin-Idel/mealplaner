package mealplaner.xml.model.v2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.xml.util.LocalDataAdapter;

@XmlRootElement(name = "MealplanerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class MealplanerDataXml {
  public int version = 2;

  @XmlElementWrapper(name = "defaultSettings")
  @XmlElement(name = "setting")
  public Map<DayOfWeek, SettingsXml> defaultSettings;
  @XmlElement(name = "dateSinceUpdate")
  @XmlJavaTypeAdapter(value = LocalDataAdapter.class)
  public LocalDate date;
  public ProposalXml proposal;
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public final List<MealXml> meals;
  @XmlElementWrapper(name = "ingredients")
  @XmlElement(name = "ingredient")
  public final List<IngredientXml> ingredients;

  public MealplanerDataXml() {
    this(new HashMap<>(), LocalDate.MIN, new ProposalXml(), new ArrayList<>(), new ArrayList<>());
  }

  public MealplanerDataXml(Map<DayOfWeek, SettingsXml> defaultSettings, LocalDate date,
      ProposalXml proposal, List<MealXml> meals, List<IngredientXml> ingredients) {
    this.defaultSettings = defaultSettings;
    this.date = date;
    this.proposal = proposal;
    this.meals = meals;
    this.ingredients = ingredients;
  }
}
