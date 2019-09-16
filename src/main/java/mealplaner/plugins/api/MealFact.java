package mealplaner.plugins.api;

public interface MealFact {
  default MealFactXml convertToXml() {
    return (MealFactXml) this;
  }
}
