// SPDX-License-Identifier: MIT

package mealplaner.plugins.comment;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

public class CommentBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CommentMessagesBundle", "comment");
  }
}
