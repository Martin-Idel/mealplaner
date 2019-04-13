// SPDX-License-Identifier: MIT

package mealplaner.model.settings.subsettings;

import mealplaner.model.meal.Meal;

interface CookingSetting {
  boolean prohibits(Meal meal);
}
