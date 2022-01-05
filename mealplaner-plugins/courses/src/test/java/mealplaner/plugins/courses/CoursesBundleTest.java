// SPDX-License-Identifier: MIT

package mealplaner.plugins.courses;

import static bundletests.BundleCommons.allMessageTests;

import java.io.File;

import org.junit.jupiter.api.Test;

class CoursesBundleTest {
  @Test
  void testBundle() {
    allMessageTests("CoursesMessagesBundle", "plugins" + File.separator + "courses");
  }
}
