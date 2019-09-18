package mealplaner.io.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.plugins.ModelExtension;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;

public class FactsAdapter {
  public static <FactType extends Fact, FactTypeXml extends FactXml> Map<Class, FactType> extractFacts(
      List<Object> facts, ModelExtension<FactType, FactTypeXml> knownExtensions) {
    var mealFactMap = new HashMap<Class, FactType>();
    addSavedFacts(facts, knownExtensions, mealFactMap);
    defaultUnsavedMealFacts(knownExtensions, mealFactMap);
    return mealFactMap;
  }

  @SuppressWarnings("unchecked")
  private static <FactType extends Fact, FactTypeXml extends FactXml> void addSavedFacts(
      List<Object> mealFacts, ModelExtension<FactType, FactTypeXml> knownExtensions, HashMap<Class, FactType> mealFactMap) {
    for (var potentialKnownMealFact : mealFacts) {
      if (knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        var castMealFactXml = (FactTypeXml) potentialKnownMealFact;
        mealFactMap.put(
            knownExtensions.obtainFactClass(castMealFactXml.getClass()),
            (FactType) castMealFactXml.convertToFact());
      }
    }
  }

  private static <FactType extends Fact, FactTypeXml extends FactXml> void defaultUnsavedMealFacts(
      ModelExtension<FactType, FactTypeXml> knownPlugins, HashMap<Class, FactType> mealFactMap) {
    for (var extension : knownPlugins.getAllRegisteredFacts()) {
      if (!mealFactMap.containsKey(extension)) {
        mealFactMap.put(extension, knownPlugins.getDefault(extension));
      }
    }
  }

  // TODO probably add test
  public static <FactType extends Fact, FactTypeXml extends FactXml> List<Element> extractUnknownFacts(
      List<Object> facts, ModelExtension<FactType, FactTypeXml> knownExtensions) {
    var potentialFact = new ArrayList<Element>();
    for (var potentialKnownMealFact : facts) {
      if (!knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        potentialFact.add((Element) potentialKnownMealFact);
      }
    }
    return potentialFact;
  }
}
