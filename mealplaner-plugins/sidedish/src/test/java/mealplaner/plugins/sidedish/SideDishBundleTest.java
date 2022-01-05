// SPDX-License-Identifier: MIT

package mealplaner.plugins.sidedish;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class SideDishBundleTest {
  @Test
  void testBundle() {
    allMessageTests("SideDishMessagesBundle", "plugins" + File.separator + "sidedish");
  }
}
