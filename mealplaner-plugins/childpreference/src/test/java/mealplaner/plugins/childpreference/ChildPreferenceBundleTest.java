// SPDX-License-Identifier: MIT

package mealplaner.plugins.childpreference;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class ChildPreferenceBundleTest {
  @Test
  void testBundle() {
    allMessageTests("ChildPreferenceMessagesBundle", "plugins" + File.separator + "childpreference");
  }
}