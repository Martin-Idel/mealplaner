// SPDX-License-Identifier: MIT

package mealplaner.plugins.plugins.cookingtime.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CookingTime {
  VERY_SHORT(BUNDLES.message("veryshort")),
  SHORT(BUNDLES.message("short")),
  MEDIUM(BUNDLES.message("medium")),
  LONG(BUNDLES.message("long"));

  private final String message;

  CookingTime(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
