package mealplaner.model.enums;

import static mealplaner.commons.BundleStore.BUNDLES;

public enum CookingTime {
  VERY_SHORT(BUNDLES.message("veryshort")),
  SHORT(BUNDLES.message("short")),
  MEDIUM(BUNDLES.message("medium")),
  LONG(BUNDLES.message("long"));

  private String message;

  CookingTime(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
