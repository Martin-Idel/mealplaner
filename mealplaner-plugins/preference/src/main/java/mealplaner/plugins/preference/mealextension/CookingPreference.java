// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CookingPreference {
  VERY_POPULAR(BUNDLES.message("veryPopular")),
  NO_PREFERENCE(BUNDLES.message("noPreference")),
  RARE(BUNDLES.message("seldom"));

  private final String message;

  CookingPreference(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}