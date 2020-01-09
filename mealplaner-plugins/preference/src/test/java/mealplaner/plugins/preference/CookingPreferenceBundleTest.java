// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

public class CookingPreferenceBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CookingPreferenceMessagesBundle", "plugins/preference");
  }
}
