// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import mealplaner.model.meal.Meal;
import mealplaner.model.meal.enums.Sidedish;

final class ProposalFunctions {
  private ProposalFunctions() {
  }

  public static boolean useDifferentSidedish(Meal meal, Meal main) {
    return meal.getSidedish() == Sidedish.NONE || meal.getSidedish() != main.getSidedish();
  }
}
