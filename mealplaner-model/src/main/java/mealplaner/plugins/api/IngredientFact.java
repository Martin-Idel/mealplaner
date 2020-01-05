package mealplaner.plugins.api;

public interface IngredientFact extends Fact {
  @Override
  IngredientFactXml convertToXml();
}
