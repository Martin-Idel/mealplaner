package mealplaner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.IngredientFact;
import mealplaner.plugins.api.IngredientFactXml;

public class IngredientExtensions {
  private static final Logger LOGGER = LoggerFactory.getLogger(IngredientExtensions.class);
  private Map<Class<? extends IngredientFact>, Class<? extends IngredientFactXml>>
      registeredIngredientExtensions = new HashMap<>();
  private Map<Class<? extends IngredientFact>, Supplier<? extends IngredientFact>> defaultFactories = new HashMap<>();

  public boolean containsFact(Class<?> fact) {
    return registeredIngredientExtensions.containsKey(fact);
  }

  public boolean containsFactXml(Class<?> fact) {
    return registeredIngredientExtensions.containsValue(fact);
  }

  public Class<? extends IngredientFact> obtainFactClass(Class<? extends IngredientFactXml> factXml) {
    var ingredientFactExtension = registeredIngredientExtensions
        .entrySet()
        .stream()
        .filter(entry -> factXml.equals(entry.getValue()))
        .map(Map.Entry::getKey)
        .findFirst();
    if (ingredientFactExtension.isEmpty()) {
      throw new MealException("IngredientFactXml class unknown. Please use containsIngredientFactXml first.");
    } else {
      return ingredientFactExtension.get();
    }
  }

  public Set<Class<? extends IngredientFact>> getAllRegisteredFacts() {
    return registeredIngredientExtensions.keySet();
  }

  public Collection<Class<? extends IngredientFactXml>> getAllRegisteredFactXmls() {
    return registeredIngredientExtensions.values();
  }

  public IngredientFact getDefault(Class<? extends IngredientFact> fact) {
    return defaultFactories.get(fact).get();
  }

  public <T extends IngredientFact> void registerClass(
      Class<T> fact,
      Class<? extends IngredientFactXml> factXml,
      Supplier<T> factSupplier) {
    registeredIngredientExtensions.putIfAbsent(fact, factXml);
    defaultFactories.putIfAbsent(fact, factSupplier);
  }
}
