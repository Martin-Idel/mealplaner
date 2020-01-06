// SPDX-License-Identifier: MIT

package mealplaner.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.FactXml;

public class ModelExtension<FactT extends Fact, FactXmlT extends FactXml> {
  private Map<Class<? extends Fact>, Class<? extends FactXml>>
      registeredMealExtensions = new HashMap<>();
  private Map<Class<? extends Fact>, Supplier<? extends FactT>> defaultFactories = new HashMap<>();

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

  public FactT getDefault(Class<? extends Fact> fact) {
    return (FactT) defaultFactories.get(fact).get();
  }

  public <T extends FactT> void registerClass(
      Class<T> fact,
      Class<? extends FactXmlT> factXml,
      Supplier<T> factSupplier) {
    if (registeredMealExtensions.containsKey(fact)) {
      throw new MealException("Class name " + fact + " already known or plugin already registered");
    }
    if (registeredMealExtensions.containsValue(factXml)) {
      throw new MealException("Class name " + fact + " already known or plugin already registered");
    }
    registeredMealExtensions.putIfAbsent(fact, factXml);
    defaultFactories.putIfAbsent(fact, factSupplier);
  }
}
