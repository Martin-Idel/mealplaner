package mealplaner.plugins.api;

public interface MealFactXml extends FactXml {
  @Override
  default MealFact convertToFact() {
    return (MealFact) this;
  }
}

