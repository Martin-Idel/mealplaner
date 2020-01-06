// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

public interface IngredientFact extends Fact {
  @Override
  IngredientFactXml convertToXml();
}
