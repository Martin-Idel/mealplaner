// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class RecipePhotoBundleTest {
  @Test
  void testBundle() {
    allMessageTests("RecipePhotoMessagesBundle", "plugins" + File.separator + "recipephoto");
  }
}
