// SPDX-License-Identifier: MIT

package mealplaner.model.settings.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CourseSettings {
  ONLY_MAIN(BUNDLES.message("onlymain")),
  MAIN_DESERT(BUNDLES.message("maindesert")),
  ENTRY_MAIN(BUNDLES.message("entrymain")),
  THREE_COURSE(BUNDLES.message("threecourse"));

  private String message;

  CourseSettings(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
