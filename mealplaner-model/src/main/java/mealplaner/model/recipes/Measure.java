// SPDX-License-Identifier: MIT

package mealplaner.model.recipes;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum Measure {
  GRAM(BUNDLES.message("GRAM")),
  MILLILITRE(BUNDLES.message("MILLILITRE")),
  TEASPOON(BUNDLES.message("TEASPOON")),
  TABLESPOON(BUNDLES.message("TABLESPOON")),
  AMOUNT(BUNDLES.message("AMOUNT")),
  BUNDLE(BUNDLES.message("BUNDLE")),
  HANDFUL(BUNDLES.message("HANDFUL")),
  NONE(BUNDLES.message("NONE"));

  private final String message;

  Measure(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
