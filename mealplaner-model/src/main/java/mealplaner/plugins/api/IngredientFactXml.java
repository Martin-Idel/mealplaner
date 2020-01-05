package mealplaner.plugins.api;

public interface IngredientFactXml extends FactXml {
  @Override
  IngredientFact convertToFact();
}
