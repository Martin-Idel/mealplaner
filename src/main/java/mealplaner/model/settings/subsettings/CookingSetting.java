// SPDX-License-Identifier: MIT

package mealplaner.model.settings.subsettings;

import mealplaner.model.meal.Meal;

public interface CookingSetting {
  boolean prohibits(Meal meal);
}
