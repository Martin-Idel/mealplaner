package mealplaner.plugins.api;

public interface MealFactXml {
  default MealFact convertToFact() {
    return (MealFact) this;
  }
}

