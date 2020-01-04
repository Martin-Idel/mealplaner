// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference.settingextension;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum PreferenceSettings {
  NORMAL(BUNDLES.message("normalPref")),
  VERY_POPULAR_ONLY(BUNDLES.message("veryPopularOnlyPref")),
  RARE_NONE(BUNDLES.message("noSeldomPref")),
  RARE_PREFERED(BUNDLES.message("seldomPref"));

  private final String message;

  PreferenceSettings(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
