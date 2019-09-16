package mealplaner.io.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.MealExtensions;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

public class FactsAdapter {
  public static Map<Class, MealFact> extractMealFacts(List<Object> mealFacts, MealExtensions knownExtensions) {
    var mealFactMap = new HashMap<Class, MealFact>();
    addSavedFacts(mealFacts, knownExtensions, mealFactMap);
    defaultUnsavedMealFacts(knownExtensions, mealFactMap);
    return mealFactMap;
  }

  private static void addSavedFacts(
      List<Object> mealFacts, MealExtensions knownExtensions, HashMap<Class, MealFact> mealFactMap) {
    for (var potentialKnownMealFact : mealFacts) {
      if (knownExtensions.containsMealFactXml(potentialKnownMealFact.getClass())) {
        var castMealFactXml = (MealFactXml) potentialKnownMealFact;
        mealFactMap.put(
            knownExtensions.obtainMealFactClass(castMealFactXml.getClass()),
            castMealFactXml.convertToMeal());
      }
    }
  }

  private static void defaultUnsavedMealFacts(MealExtensions knownPlugins, HashMap<Class, MealFact> mealFactMap) {
    for (var extension: knownPlugins.getAllRegisteredFacts()) {
      if (!mealFactMap.containsKey(extension)) {
        mealFactMap.put(extension, knownPlugins.getDefault(extension));
      }
    }
  }

  // TODO probably add test
  public static List<Element> extractUnknownFacts(
      List<Object> mealFacts, MealExtensions knownExtensions) {
    var potentialFact = new ArrayList<Element>();
    for (var potentialKnownMealFact : mealFacts) {
      if (!knownExtensions.containsMealFactXml(potentialKnownMealFact.getClass())) {
        potentialFact.add((Element) potentialKnownMealFact);
      }
    }
    return potentialFact;
  }
}
