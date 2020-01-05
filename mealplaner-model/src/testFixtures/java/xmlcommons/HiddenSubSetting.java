package xmlcommons;

import java.util.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HiddenSubSetting implements Setting, SettingXml {
  public enum HiddenEnum {
    TEST1, TEST2;
  }

  private final HiddenEnum hiddenEnum;

  public HiddenSubSetting(HiddenEnum hiddenEnum) {
    this.hiddenEnum = hiddenEnum;
  }

  public HiddenSubSetting() {
    this.hiddenEnum = HiddenEnum.TEST1;
  }

  public HiddenEnum getHiddenEnum() {
    return hiddenEnum;
  }

  @Override
  public Fact convertToFact() {
    return this;
  }

  @Override
  public FactXml convertToXml() {
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
    HiddenSubSetting that = (HiddenSubSetting) o;
    return hiddenEnum == that.hiddenEnum;
  }

  @Override
  public int hashCode() {
    return Objects.hash(hiddenEnum);
  }

  @Override
  public String toString() {
    return "[hiddenEnum=" + hiddenEnum + "]";
  }
}
