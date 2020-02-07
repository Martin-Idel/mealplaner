// SPDX-License-Identifier: MIT

package mealplaner.plugins.sidedish;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

public class SideDishBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("SideDishMessagesBundle", "plugins" + File.separator + "sidedish");
  }
}
