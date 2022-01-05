// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import mealplaner.io.xml.util.LocalDataAdapter;

@XmlRootElement(name = "MealplanerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProposalSummaryDataXml {
  private int version = 3;
  @XmlElementWrapper(name = "defaultSettings")
  @XmlElement(name = "setting")
  public final Map<DayOfWeek, SettingsXml> defaultSettings;
  @XmlElement(name = "dateSinceUpdate")
  @XmlJavaTypeAdapter(value = LocalDataAdapter.class)
  public final LocalDate date;
  public final ProposalXml proposal;

  public ProposalSummaryDataXml() {
    this(new EnumMap<>(DayOfWeek.class), LocalDate.MIN, new ProposalXml());
  }

  public ProposalSummaryDataXml(Map<DayOfWeek, SettingsXml> defaultSettings,
      LocalDate date,
      ProposalXml proposal) {
    this.defaultSettings = defaultSettings;
    this.date = date;
    this.proposal = proposal;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
