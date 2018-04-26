package mealplaner.commons.gui;

import static java.nio.file.Files.readAllLines;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.Utils.getLocalizedResource;
import static mealplaner.commons.gui.MessageDialog.errorMessages;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

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
    try {
      URL resource = getLocalizedResource(resourcePath + resourceName, suffix);
      readAllLines(Paths.get(resource.toURI()))
          .forEach(string -> content.append(string + "\n"));
    } catch (IOException exc) {
      errorMessages(null, exc, BUNDLES.errorMessage("MSG_FAIL_HELP"));
      logger.error("The specified file does not exist. ", exc);
    } catch (URISyntaxException exc) {
      errorMessages(null, exc, BUNDLES.errorMessage("MSG_FAIL_HELP"));
      logger.error(
          "Something went wrong with retrieving the URL for the help site. "
              + "This should not happen, please contact the developer. ",
          exc);
    }
    editorPane.setText(content.toString());
    editorPane.setSize(500, 400);

    return new JScrollPane(editorPane);
  }
}
