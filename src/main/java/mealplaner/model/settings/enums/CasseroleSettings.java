// SPDX-License-Identifier: MIT

package mealplaner.model.settings.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CasseroleSettings {
  POSSIBLE(BUNDLES.message("allowedCasseroles")),
  ONLY(BUNDLES.message("onlyCasseroles")),
  NONE(BUNDLES.message("noCasseroles"));

  private final String message;

  CasseroleSettings(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
