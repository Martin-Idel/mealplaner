// SPDX-License-Identifier: MIT

package mealplaner.plugins.api;

public interface MealFact extends Fact {
  @Override
  default MealFactXml convertToXml() {
    return (MealFactXml) this;
  }
}
