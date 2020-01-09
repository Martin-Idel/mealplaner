// SPDX-License-Identifier: MIT

package mealplaner.plugins.cookingtime;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

public class CommentBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CommentMessagesBundle", "comment");
  }
}
