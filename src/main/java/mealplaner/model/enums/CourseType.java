package mealplaner.model.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CourseType {
  ENTRY(BUNDLES.message("entry")),
  MAIN(BUNDLES.message("main")),
  DESERT(BUNDLES.message("desert"));

  private String message;

  CourseType(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
