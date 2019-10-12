package testcommons;

import java.util.Objects;

import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;
import mealplaner.plugins.api.Setting;
import mealplaner.plugins.api.SettingXml;

public class TestSubSetting implements Setting, SettingXml {
  public enum TestSetting {
    TEST1, TEST2
  }

  private final TestSetting setting;

  public TestSubSetting() {
    this.setting = TestSetting.TEST1;
  }

  public TestSubSetting(TestSetting setting) {
    this.setting = setting;
  }

  public TestSetting getSetting() {
    return setting;
  }

  @Override
  public FactXml convertToXml() {
    return this;
  }

  @Override
  public Fact convertToFact() {
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
    TestSubSetting that = (TestSubSetting) o;
    return setting == that.setting;
  }

  @Override
  public int hashCode() {
    return Objects.hash(setting);
  }

  @Override
  public String toString() {
    return "[setting=" + setting + ']';
  }
}
