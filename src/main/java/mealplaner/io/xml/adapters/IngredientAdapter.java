// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.util.FactsAdapter.extractFacts;
import static mealplaner.io.xml.util.FactsAdapter.extractUnknownFacts;
import static mealplaner.model.recipes.IngredientBuilder.ingredientWithValidation;
import static mealplaner.model.recipes.Measures.createMeasures;

import java.util.ArrayList;
import java.util.stream.Collectors;

import mealplaner.io.xml.model.v2.IngredientXml;
import mealplaner.io.xml.model.v3.MeasuresXml;
import mealplaner.model.recipes.Ingredient;
import mealplaner.model.recipes.IngredientBuilder;
import mealplaner.model.recipes.Measures;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.IngredientFact;

public final class IngredientAdapter {
  private IngredientAdapter() {
  }

  public static IngredientXml convertIngredientV2ToXml(Ingredient ingredient) {
    return new IngredientXml(ingredient.getId(),
        ingredient.getName(),
        ingredient.getType(),
        ingredient.getPrimaryMeasure());
  }

  public static mealplaner.io.xml.model.v3.IngredientXml convertIngredientV3ToXml(Ingredient ingredient) {
    var ingredientFacts = new ArrayList<Object>();
    ingredientFacts.addAll(
        ingredient.getIngredientFacts()
            .values()
            .stream()
            .map(IngredientFact::convertToXml)
            .collect(Collectors.toUnmodifiableList()));
    ingredientFacts.addAll(ingredient.getHiddenFacts());
    return new mealplaner.io.xml.model.v3.IngredientXml(ingredient.getId(),
        ingredient.getName(),
        ingredient.getType(),
        new MeasuresXml(ingredient.getMeasures()),
        ingredientFacts);
  }

  public static Ingredient convertIngredientV2FromXml(IngredientXml ingredient) {
    return IngredientBuilder.ingredient()
        .withUuid(ingredient.uuid)
        .withName(ingredient.name)
        .withType(ingredient.type)
        .withPrimaryMeasure(ingredient.measure)
        .create();
  }

  public static Ingredient convertIngredientV3FromXml(
      mealplaner.io.xml.model.v3.IngredientXml ingredient,
      PluginStore plugins) {
    var registeredExtensions = plugins.getRegisteredIngredientExtensions();
    return ingredientWithValidation(registeredExtensions.getAllRegisteredFacts())
        .withUuid(ingredient.uuid)
        .withName(ingredient.name)
        .withType(ingredient.type)
        .withMeasures(createMeasures(ingredient.measures.primaryMeasure, ingredient.measures.secondaryMeasures))
        .withFacts(extractFacts(ingredient.ingredientFacts, registeredExtensions))
        .withHiddenFacts(extractUnknownFacts(ingredient.ingredientFacts, registeredExtensions))
        .create();
  }
}
