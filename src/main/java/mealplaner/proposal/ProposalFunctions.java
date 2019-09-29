// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.ObligatoryUtensil;
import mealplaner.model.meal.enums.Sidedish;
import mealplaner.model.settings.Settings;

final class ProposalFunctions {
  private ProposalFunctions() {
  }

  public static boolean useDifferentSidedish(Meal meal, Meal main) {
    return meal.getSidedish() == Sidedish.NONE || meal.getSidedish() != main.getSidedish();
  }

  public static boolean useDifferentUtensil(Meal meal, Meal main) {
    switch (main.getObligatoryUtensil()) {
      case CASSEROLE:
        return meal.getObligatoryUtensil() != ObligatoryUtensil.CASSEROLE;
      case PAN:
        return meal.getObligatoryUtensil() != ObligatoryUtensil.PAN;
      default:
        return true;
    }
  }

  public static boolean allows(Meal meal, Settings settings) {
    return !(settings.getCookingUtensil().prohibits(meal)
        || settings.getCookingPreference().prohibits(meal));
  }
}
