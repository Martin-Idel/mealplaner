// SPDX-License-Identifier: MIT

package integrationtests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class SpdxIdentifierTest {

  @Test
  public void allJavaFilesContainSpdxIdentifier() {
    try {
      List<Path> filesWithErrors = Files
          .find(Paths.get(".."), 999,
              (p, bfa) -> bfa.isRegularFile() && p.getFileName().toString().endsWith(".java"))
          .filter(this::containsSpdxIdentifier)
          .collect(Collectors.toList());
      assertThat(filesWithErrors).isEmpty();
    } catch (IOException e) {
      fail(e.getMessage());
    }
  }

  private boolean containsSpdxIdentifier(Path path) {
    try {
      return !Files
          .lines(path, StandardCharsets.UTF_8)
          .findFirst()
          .map(line -> line.startsWith("// SPDX-License-Identifier: MIT"))
          .orElse(false);
    } catch (IOException e) {
      fail(e.getMessage());
      return false;
    }
  }
}
