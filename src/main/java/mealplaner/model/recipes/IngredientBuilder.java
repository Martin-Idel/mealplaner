// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static java.util.UUID.randomUUID;
import static mealplaner.model.recipes.Ingredient.ingredientWithUuid;
import static mealplaner.model.recipes.IngredientType.OTHER;
import static mealplaner.model.recipes.Measures.createMeasures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeFraction;
import mealplaner.commons.errorhandling.MealException;
import mealplaner.plugins.api.Fact;
import mealplaner.plugins.api.IngredientFact;

public class IngredientBuilder {
  private Set<Class<? extends Fact>> validationStore;
  private UUID uuid;
  private String name = "";
  private IngredientType type = OTHER;
  private Measure primaryMeasure = Measure.NONE;
  private Map<Measure, NonnegativeFraction> secondaryMeasures = new HashMap<>();
  private Map<Class, IngredientFact> facts = new HashMap<>();
  private List<Element> hiddenFacts = new ArrayList<>();

  private IngredientBuilder() {
  }

  private IngredientBuilder(Set<Class<? extends Fact>> validationStore) {
    this.validationStore = validationStore;
  }

  public static IngredientBuilder ingredientWithValidation(Set<Class<? extends Fact>> validationStore) {
    return new IngredientBuilder(validationStore);
  }

  public static IngredientBuilder ingredient() {
    return new IngredientBuilder();
  }

  public static IngredientBuilder from(Ingredient ingredient) {
    return new IngredientBuilder()
        .withUuid(ingredient.getId())
        .withType(ingredient.getType())
        .withMeasures(ingredient.getMeasures())
        .withFacts(ingredient.getIngredientFacts())
        .withHiddenFacts(ingredient.getHiddenFacts());
  }

  public IngredientBuilder withHiddenFacts(List<Element> hiddenFacts) {
    this.hiddenFacts = hiddenFacts;
    return this;
  }

  public IngredientBuilder withFacts(Map<Class, IngredientFact> ingredientFacts) {
    this.facts.putAll(ingredientFacts);
    return this;
  }

  public IngredientBuilder addFact(IngredientFact fact) {
    this.facts.put(fact.getClass(), fact);
    return this;
  }

  public IngredientBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public IngredientBuilder withUuid(UUID uuid) {
    this.uuid = uuid;
    return this;
  }

  public IngredientBuilder withType(IngredientType type) {
    this.type = type;
    return this;
  }

  public IngredientBuilder withPrimaryMeasure(Measure measure) {
    this.primaryMeasure = measure;
    return this;
  }

  public SecondaryMeasureBuilder withSecondaryMeasure(Measure measure) {
    return new SecondaryMeasureBuilder(measure, this);
  }

  public IngredientBuilder withMeasures(Measures measures) {
    this.primaryMeasure = measures.getPrimaryMeasure();
    this.secondaryMeasures = measures.getSecondaries();
    return this;
  }

  public Ingredient create() {
    if (validationStore != null) {
      validateFacts();
    }
    return ingredientWithUuid(
        uuid == null ? randomUUID() : uuid,
        name,
        type, createMeasures(primaryMeasure, secondaryMeasures),
        facts,
        hiddenFacts);
  }

  private void validateFacts() {
    for (var fact : validationStore) {
      if (!facts.containsKey(fact)) {
        throw new MealException("Class does not contain fact of type " + fact);
      }
    }
  }

  public static class SecondaryMeasureBuilder {
    private final Measure measure;
    private final IngredientBuilder ingredientBuilder;

    private SecondaryMeasureBuilder(Measure measure, IngredientBuilder ingredientBuilder) {
      this.measure = measure;
      this.ingredientBuilder = ingredientBuilder;
    }

    public IngredientBuilder measureInPrimaryMeasure(NonnegativeFraction conversionFactor) {
      ingredientBuilder.secondaryMeasures.put(measure, conversionFactor);
      return ingredientBuilder;
    }
  }
}
