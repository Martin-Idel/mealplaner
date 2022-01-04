// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

public class RecipePhotoBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("RecipePhotoMessagesBundle", "plugins" + File.separator + "recipephoto");
  }
}
