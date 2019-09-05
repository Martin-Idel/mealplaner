// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import mealplaner.commons.NonnegativeFraction;

public class IngredientBuilder {
  private UUID uuid;
  private String name;
  private IngredientType type;
  private Measure primaryMeasure;
  private Map<Measure, NonnegativeFraction> secondaryMeasures;

  private IngredientBuilder() {
    name = "";
    type = IngredientType.OTHER;
    primaryMeasure = Measure.NONE;
    secondaryMeasures = new HashMap<>();
  }

  public static IngredientBuilder ingredient() {
    return new IngredientBuilder();
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

  public Ingredient create() {
    return uuid == null ? Ingredient.ingredient(name, type, Measures.createMeasures(primaryMeasure, secondaryMeasures))
        : Ingredient.ingredientWithUuid(uuid, name, type, Measures.createMeasures(primaryMeasure, secondaryMeasures));
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
