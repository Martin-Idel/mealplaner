package mealplaner.commons.gui;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.Utils.getLocalizedResource;
import static mealplaner.commons.gui.MessageDialog.userErrorMessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

  public static JScrollPane scrollPaneWithTextFrom(
      String resourcePath, String resourceName, String suffix) {
    JEditorPane editorPane = new JEditorPane();
    editorPane.setEditable(false);

    editorPane.setContentType("text/plain");
    StringBuilder content = new StringBuilder();
    InputStream in = HelpPanel.class
        .getResourceAsStream(getLocalizedResource(resourcePath + resourceName, suffix));
    if (in == null) {
      userErrorMessage(null, BUNDLES.errorMessage("MSG_FAIL_HELP"));
      logger.error("The specified resource does not exist. ");
    } else {
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      reader.lines().forEach(line -> content.append(line + "\n"));
    }
    editorPane.setText(content.toString());
    editorPane.setSize(500, 400);

    return new JScrollPane(editorPane);
  }
}
