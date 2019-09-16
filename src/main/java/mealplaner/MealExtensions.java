package mealplaner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.MealFact;
import mealplaner.plugins.api.MealFactXml;

public class MealExtensions {
  private static final Logger LOGGER = LoggerFactory.getLogger(MealExtensions.class);
  private Map<Class<? extends MealFact>, Class<? extends MealFactXml>>
      registeredMealExtensions = new HashMap<>();
  private Map<Class<? extends MealFact>, Supplier<? extends MealFact>> defaultFactories = new HashMap<>();

  public boolean containsFact(Class<?> fact) {
    return registeredMealExtensions.containsKey(fact);
  }

  public boolean containsFactXml(Class<?> fact) {
    return registeredMealExtensions.containsValue(fact);
  }

  public Class<? extends MealFact> obtainFactClass(Class<? extends MealFactXml> factXml) {
    var mealFactExtension = registeredMealExtensions
        .entrySet()
        .stream()
        .filter(entry -> factXml.equals(entry.getValue()))
        .map(Map.Entry::getKey)
        .findFirst();
    if (mealFactExtension.isEmpty()) {
      throw new MealException("MealFactXml class unknown. Please use containsMealFactXml first.");
    } else {
      return mealFactExtension.get();
    }
  }

  public Set<Class<? extends MealFact>> getAllRegisteredFacts() {
    return registeredMealExtensions.keySet();
  }

  public Collection<Class<? extends MealFactXml>> getAllRegisteredFactXmls() {
    return registeredMealExtensions.values();
  }

  public MealFact getDefault(Class<? extends MealFact> fact) {
    return defaultFactories.get(fact).get();
  }

  public <T extends MealFact> void registerClass(
      Class<T> fact,
      Class<? extends MealFactXml> factXml,
      Supplier<T> factSupplier) {
    registeredMealExtensions.putIfAbsent(fact, factXml);
    defaultFactories.putIfAbsent(fact, factSupplier);
  }
}
