// SPDX-License-Identifier: MIT

package mealplaner.plugins.comment;

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

class CommentBundleTest {
  @Test
  void testBundle() {
    allMessageTests("CommentMessagesBundle", "comment");
  }
}
