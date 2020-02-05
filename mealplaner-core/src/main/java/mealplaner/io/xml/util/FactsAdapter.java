// SPDX-License-Identifier: MIT

package mealplaner.io.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;

public final class FactsAdapter {

  private FactsAdapter() {
  }

  public static <FactT extends Fact, FactXmlT extends FactXml> Map<Class, FactT> extractFacts(
      List<Object> facts, ModelExtension<FactT, FactXmlT> knownExtensions) {
    var mealFactMap = new HashMap<Class, FactT>();
    addSavedFacts(facts, knownExtensions, mealFactMap);
    defaultUnsavedMealFacts(knownExtensions, mealFactMap);
    return mealFactMap;
  }

  @SuppressWarnings("unchecked")
  private static <FactT extends Fact, FactXmlT extends FactXml> void addSavedFacts(
      List<Object> mealFacts, ModelExtension<FactT, FactXmlT> knownExtensions, Map<Class, FactT> mealFactMap) {
    for (var potentialKnownMealFact : mealFacts) {
      if (knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        var castMealFactXml = (FactXmlT) potentialKnownMealFact;
        mealFactMap.put(
            knownExtensions.obtainFactClass(castMealFactXml.getClass()),
            (FactT) castMealFactXml.convertToFact());
      }
    }
  }

  private static <FactT extends Fact, FactXmlT extends FactXml> void defaultUnsavedMealFacts(
      ModelExtension<FactT, FactXmlT> knownPlugins, Map<Class, FactT> mealFactMap) {
    for (var extension : knownPlugins.getAllRegisteredFacts()) {
      if (!mealFactMap.containsKey(extension)) {
        mealFactMap.put(extension, knownPlugins.getDefault(extension));
      }
    }
  }

  // TODO probably add test
  public static <FactT extends Fact, FactXmlT extends FactXml> List<Element> extractUnknownFacts(
      List<Object> facts, ModelExtension<FactT, FactXmlT> knownExtensions) {
    var potentialFact = new ArrayList<Element>();
    for (var potentialKnownMealFact : facts) {
      if (!knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        potentialFact.add((Element) potentialKnownMealFact);
      }
    }
    return potentialFact;
  }
}
