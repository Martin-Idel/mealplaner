// SPDX-License-Identifier: MIT

package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.BundleUtils.getLocalizedResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelpPanel {
  private static final Logger logger = LoggerFactory.getLogger(HelpPanel.class);

  private HelpPanel() {
  }

  public static JScrollPane mealPlanerHelpScrollPane(String resourceName) {
    return scrollPaneWithTextFrom("/help/", resourceName, "txt");
  }

  private static JScrollPane scrollPaneWithTextFrom(
          String resourcePath, String resourceName, String suffix) {
    JEditorPane editorPane = new JEditorPane();
    editorPane.setEditable(false);

    editorPane.setContentType("text/plain");
    StringBuilder content = new StringBuilder();
    try (InputStream in = HelpPanel.class
        .getResourceAsStream(getLocalizedResource(resourcePath + resourceName, suffix));
         InputStreamReader inputReader = new InputStreamReader(in, StandardCharsets.UTF_8);
         BufferedReader reader = new BufferedReader(inputReader)) {
      reader.lines().forEach(line -> content.append(line).append("\n"));
    } catch (IOException exc) {
      logger.error("The specified resource does not exist. ", exc);
    } catch (NullPointerException exc) { // NOPMD
      MessageDialog.errorMessages(null, BUNDLES.errorMessage("MSG_FAIL_HELP"));
      logger.error("The specified resource does not exist.");
    }
    editorPane.setText(content.toString());
    editorPane.setSize(500, 400);

    return new JScrollPane(editorPane);
  }
}
