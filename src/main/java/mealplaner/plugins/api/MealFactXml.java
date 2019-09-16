package mealplaner.plugins.api;

public interface MealFactXml {
  default MealFact convertToMeal() {
    return (MealFact) this;
  }
}

