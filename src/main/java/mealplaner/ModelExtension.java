package mealplaner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;

public class ModelExtension<FactType extends Fact, FactXmlType extends FactXml> {
  private static final Logger LOGGER = LoggerFactory.getLogger(ModelExtension.class);
  private Map<Class<? extends Fact>, Class<? extends FactXml>>
      registeredMealExtensions = new HashMap<>();
  private Map<Class<? extends Fact>, Supplier<? extends FactType>> defaultFactories = new HashMap<>();

  public boolean containsFact(Class<?> fact) {
    return registeredMealExtensions.containsKey(fact);
  }

  public boolean containsFactXml(Class<?> fact) {
    return registeredMealExtensions.containsValue(fact);
  }

  public Class<? extends Fact> obtainFactClass(Class<? extends FactXml> factXml) {
    var factExtension = registeredMealExtensions
        .entrySet()
        .stream()
        .filter(entry -> factXml.equals(entry.getValue()))
        .map(Map.Entry::getKey)
        .findFirst();
    if (factExtension.isEmpty()) {
      throw new MealException("Fact xml class unknown. Please use containsFactXml first.");
    } else {
      return factExtension.get();
    }
  }

  public Set<Class<? extends Fact>> getAllRegisteredFacts() {
    return registeredMealExtensions.keySet();
  }

  public Collection<Class<? extends FactXml>> getAllRegisteredFactXmls() {
    return registeredMealExtensions.values();
  }

  public FactType getDefault(Class<? extends Fact> fact) {
    return (FactType) defaultFactories.get(fact).get();
  }

  public <T extends FactType> void registerClass(
      Class<T> fact,
      Class<? extends FactXmlType> factXml,
      Supplier<T> factSupplier) {
    registeredMealExtensions.putIfAbsent(fact, factXml);
    defaultFactories.putIfAbsent(fact, factSupplier);
  }
}
