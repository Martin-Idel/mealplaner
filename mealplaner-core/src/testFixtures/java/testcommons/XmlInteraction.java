// SPDX-License-Identifier: MIT

package testcommons;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.After;

public class XmlInteraction {
  protected static final String DESTINATION_FILE_PATH = "src/test/resources/saveTemp.xml";

  @After
  public void tearDown() {
    try {
      File file = new File(DESTINATION_FILE_PATH);
      if (file.exists()) {
        Files.delete(file.toPath());
      }
    } catch (IOException ioex) {
      fail("Something went wrong with the TearDown");
    }
  }

  protected void loadFileWithName(String fileName) {
    File originalFile = new File(fileName);
    File temporaryFile = new File(DESTINATION_FILE_PATH);
    makeTemporaryDirectory(DESTINATION_FILE_PATH);
    try {
      Files.copy(originalFile.toPath(), temporaryFile.toPath(), REPLACE_EXISTING);
    } catch (IOException exc) {
      fail("Could not load file");
    }
  }

  private static void makeTemporaryDirectory(String filepath) {
    var tempdir = new File(filepath).getParentFile();
    if (!tempdir.getParentFile().exists()) {
      var mkdirs = tempdir.getParentFile().mkdirs();
      if (!mkdirs) {
        fail("Could not create temporary directories");
      }
    }
  }
}
