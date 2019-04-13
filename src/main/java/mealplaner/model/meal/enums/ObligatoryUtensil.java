// SPDX-License-Identifier: MIT

package mealplaner.model.meal.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum ObligatoryUtensil {
  POT(BUNDLES.message("pot")),
  PAN(BUNDLES.message("pan")),
  CASSEROLE(BUNDLES.message("casserole"));

  private final String message;

  ObligatoryUtensil(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
