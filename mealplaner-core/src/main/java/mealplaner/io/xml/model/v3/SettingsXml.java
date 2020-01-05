// SPDX-License-Identifier: MIT

package mealplaner.io.xml.model.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettingsXml {
  public final int numberOfPeople;
  @XmlAnyElement(lax = true)
  public final List<Object> settings;

  public SettingsXml() {
    this(1, new ArrayList<>());
  }

  public SettingsXml(
      int numberOfPeople,
      List<Object> settings) {
    this.numberOfPeople = numberOfPeople;
    this.settings = settings;
  }
}
