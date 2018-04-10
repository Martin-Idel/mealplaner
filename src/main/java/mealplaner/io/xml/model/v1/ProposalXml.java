package mealplaner.io.xml.model.v1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.io.xml.util.LocalDataAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProposalXml {
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public List<MealXml> mealList;
  @XmlElementWrapper(name = "settings")
  @XmlElement(name = "setting")
  public List<SettingsXml> settingsList;
  @XmlJavaTypeAdapter(value = LocalDataAdapter.class)
  public LocalDate date;
  public boolean includeToday;

  public ProposalXml() {
    this(new ArrayList<>(), new ArrayList<>(), LocalDate.MIN, true);
  }

  public ProposalXml(List<MealXml> mealList, List<SettingsXml> settingsList, LocalDate date,
      boolean includeToday) {
    this.mealList = mealList;
    this.settingsList = settingsList;
    this.date = date;
    this.includeToday = includeToday;
  }
}
