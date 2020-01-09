// SPDX-License-Identifier: MIT

import static bundletests.BundleCommons.allMessageTests;

import org.junit.jupiter.api.Test;

public class CommentBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CoursesMessagesBundle", "plugins/courses");
  }
}
