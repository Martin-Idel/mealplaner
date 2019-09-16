package mealplaner.io.xml.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import mealplaner.IngredientExtensions;
import mealplaner.MealExtensions;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

// TODO: a lot of quasi duplicate code here (maybe extract very small PluginExtensionsInterface + static method?)
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
      if (knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        var castMealFactXml = (MealFactXml) potentialKnownMealFact;
        mealFactMap.put(
            knownExtensions.obtainFactClass(castMealFactXml.getClass()),
            castMealFactXml.convertToFact());
      }
    }
  }

  private static void addSavedFacts(
      List<Object> mealFacts, IngredientExtensions knownExtensions, HashMap<Class, IngredientFact> mealFactMap) {
    for (var potentialKnownMealFact : mealFacts) {
      if (knownExtensions.containsFact(potentialKnownMealFact.getClass())) {
        var castIngredientFactXml = (IngredientFactXml) potentialKnownMealFact;
        mealFactMap.put(
            knownExtensions.obtainFactClass(castIngredientFactXml.getClass()),
            castIngredientFactXml.convertToFact());
      }
    }
  }

  private static void defaultUnsavedMealFacts(MealExtensions knownPlugins, HashMap<Class, MealFact> mealFactMap) {
    for (var extension : knownPlugins.getAllRegisteredFacts()) {
      if (!mealFactMap.containsKey(extension)) {
        mealFactMap.put(extension, knownPlugins.getDefault(extension));
      }
    }
  }

  private static void defaultUnsavedMealFacts(
      IngredientExtensions knownPlugins, HashMap<Class, IngredientFact> ingredientFactMap) {
    for (var extension : knownPlugins.getAllRegisteredFacts()) {
      if (!ingredientFactMap.containsKey(extension)) {
        ingredientFactMap.put(extension, knownPlugins.getDefault(extension));
      }
    }
  }

  // TODO probably add test
  public static List<Element> extractUnknownFacts(
      List<Object> mealFacts, MealExtensions knownExtensions) {
    var potentialFact = new ArrayList<Element>();
    for (var potentialKnownMealFact : mealFacts) {
      if (!knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        potentialFact.add((Element) potentialKnownMealFact);
      }
    }
    return potentialFact;
  }

  // TODO probably add test
  public static List<Element> extractUnknownFacts(
      List<Object> mealFacts, IngredientExtensions knownExtensions) {
    var potentialFact = new ArrayList<Element>();
    for (var potentialKnownMealFact : mealFacts) {
      if (!knownExtensions.containsFactXml(potentialKnownMealFact.getClass())) {
        potentialFact.add((Element) potentialKnownMealFact);
      }
    }
    return potentialFact;
  }

  public static Map<Class, IngredientFact> extractIngredientFacts(
      List<Object> ingrdientFacts, IngredientExtensions knownExtensions) {
    var inngredientFactsMap = new HashMap<Class, IngredientFact>();
    addSavedFacts(ingrdientFacts, knownExtensions, inngredientFactsMap);
    defaultUnsavedMealFacts(knownExtensions, inngredientFactsMap);
    return inngredientFactsMap;
  }
}
