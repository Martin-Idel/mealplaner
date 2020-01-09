// SPDX-License-Identifier: MIT

package mealplaner.plugins.sidedish.mealextension;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum Sidedish {
  POTATOES(BUNDLES.message("potatoes")),
  PASTA(BUNDLES.message("pasta")),
  RICE(BUNDLES.message("rice")),
  NONE(BUNDLES.message("none"));

  private final String message;

  Sidedish(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }

}
