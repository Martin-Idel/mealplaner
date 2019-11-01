package mealplaner.plugins;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

public class ModelExtensionTest {
  private ModelExtension<MealFact, MealFactXml> modelExtension = new ModelExtension<>();

  @Test
  public void containsFactIsTrueIfFactIsReallyKnown() {
    modelExtension.registerClass(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));

    assertThat(modelExtension.containsFact(SomeTestFactXml.class)).isTrue();
    assertThat(modelExtension.containsFact(MealFact.class)).isFalse();
    assertThat(modelExtension.containsFact(MealFactXml.class)).isFalse();
    assertThat(modelExtension.containsFact(SomeUnknownTestFactXml.class)).isFalse();
  }

  @Test
  public void containsFactXmlIsTrueIfFactIsReallyKnown() {
    modelExtension.registerClass(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));

    assertThat(modelExtension.containsFactXml(SomeTestFactXml.class)).isTrue();
    assertThat(modelExtension.containsFactXml(MealFactXml.class)).isFalse();
    assertThat(modelExtension.containsFactXml(MealFact.class)).isFalse();
    assertThat(modelExtension.containsFactXml(SomeUnknownTestFactXml.class)).isFalse();
  }

  @Test
  public void obtainFactClassReturnsClassIfPresent() {
    modelExtension.registerClass(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));

    assertThat(modelExtension.obtainFactClass(SomeTestFactXml.class)).isEqualTo(SomeTestFactXml.class);
  }

  @Test(expected = MealException.class)
  public void obtainFactClassThrowsIfClassUnknown() {
    modelExtension.registerClass(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));

    assertThat(modelExtension.obtainFactClass(SomeUnknownTestFactXml.class)).isNull();
  }

  @Test
  public void getDefaultReturnsDefaultObjectOfClass() {
    modelExtension.registerClass(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));

    assertThat(modelExtension.getDefault(SomeTestFactXml.class)).isInstanceOf(SomeTestFactXml.class);
    assertThat(((SomeTestFactXml) modelExtension.getDefault(SomeTestFactXml.class)).testString)
        .isEqualToIgnoringWhitespace("test");
  }

  private static class SomeTestFactXml implements MealFactXml, MealFact {
    private final String testString;

    public SomeTestFactXml(String testString) {
      this.testString = testString;
    }
  }

  private static class SomeUnknownTestFactXml implements MealFactXml, MealFact {
    private final String testString;

    public SomeUnknownTestFactXml(String testString) {
      this.testString = testString;
    }
  }
}
