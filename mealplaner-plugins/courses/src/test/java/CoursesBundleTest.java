// SPDX-License-Identifier: MIT

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

public class CoursesBundleTest {
  @Test
  public void testBundle() {
    allMessageTests("CoursesMessagesBundle", "plugins" + File.separator + "courses");
  }
}
