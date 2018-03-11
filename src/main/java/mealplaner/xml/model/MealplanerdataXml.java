package mealplaner.xml.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "MealplanerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class MealplanerdataXml {
  public int version = 1;
  @XmlElementWrapper(name = "defaultSettings")
  @XmlElement(name = "setting")
  public Map<DayOfWeek, SettingsXml> defaultSettings;
  @XmlElement(name = "dateSinceUpdate")
  @XmlJavaTypeAdapter(value = LocalDataAdapter.class)
  public LocalDate date;
  public ProposalXml proposal;

  public MealplanerdataXml() {
    this(new HashMap<>(), LocalDate.MIN, new ProposalXml());
  }

  public MealplanerdataXml(Map<DayOfWeek, SettingsXml> defaultSettings,
      LocalDate date,
      ProposalXml proposal) {
    this.defaultSettings = defaultSettings;
    this.date = date;
    this.proposal = proposal;
  }
}
