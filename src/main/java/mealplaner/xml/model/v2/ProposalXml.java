package mealplaner.xml.model.v2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.xml.util.LocalDataAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ProposalXml {
  @XmlElementWrapper(name = "meals")
  @XmlElement(name = "meal")
  public List<ProposedMenuXml> mealList;
  @XmlJavaTypeAdapter(value = LocalDataAdapter.class)
  public LocalDate date;
  public boolean includeToday;

  public ProposalXml() {
    this(new ArrayList<>(), LocalDate.MIN, true);
  }

  public ProposalXml(List<ProposedMenuXml> mealList, LocalDate date,
      boolean includeToday) {
    this.mealList = mealList;
    this.date = date;
    this.includeToday = includeToday;
  }
}
