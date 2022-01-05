// SPDX-License-Identifier: MIT

package mealplaner.plugins.preference;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class CookingPreferenceBundleTest {
  @Test
  void testBundle() {
    allMessageTests("CookingPreferenceMessagesBundle", "plugins" + File.separator + "preference");
  }
}
