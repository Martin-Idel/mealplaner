// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

class FactsAdapterTest {
  private List<Object> unmarshalledFacts;
  private PluginStore knownPlugins;

  @BeforeEach
  void setUp() {
    unmarshalledFacts = new ArrayList<>();
    unmarshalledFacts.add(new SomeTestFactXml("known"));
    unmarshalledFacts.add(new SomeUnknownTestFactXml("unknown"));
    knownPlugins = new PluginStore();
    knownPlugins.registerMealExtension(SomeTestFactXml.class, SomeTestFactXml.class, () -> new SomeTestFactXml("test"));
  }

  @Test
  void extractMealFactsFindsKnownPluginAndCastsToCorrectClass() {
    var mealFacts = FactsAdapter.extractFacts(unmarshalledFacts, knownPlugins.getRegisteredMealExtensions());

    assertThat(mealFacts).containsOnlyKeys(SomeTestFactXml.class);
    assertThat(((SomeTestFactXml) mealFacts.get(SomeTestFactXml.class)).testString)
        .isEqualToIgnoringWhitespace("known");
  }

  @Test
  void extractMealFactsDefaultsKnownPluginAsGivenBySupplier() {
    var emptyFacts = new ArrayList<>();
    var mealFacts = FactsAdapter.extractFacts(emptyFacts, knownPlugins.getRegisteredMealExtensions());

    assertThat(mealFacts).containsOnlyKeys(SomeTestFactXml.class);
    assertThat(((SomeTestFactXml) mealFacts.get(SomeTestFactXml.class)).testString)
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
