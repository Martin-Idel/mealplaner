// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.errorMessages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecipePhotoEdit {
  private static final Logger logger = LoggerFactory.getLogger(RecipePhotoEdit.class);

  public static RecipePhotoFact copyAndSaveImage(RecipePhotoFact inputFact, String savePath) {
    var initialDirectory = inputFact
        .getPath()
        .map(Path::getParent)
        .orElse(Path.of(System.getProperty("user.home")))
        .toFile();
    var fileChooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & PNG Images", "jpg", "png");
    fileChooser.setFileFilter(filter);
    fileChooser.setCurrentDirectory(initialDirectory);
    int returnVal = fileChooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      try {
        var newPath = copyFileToNewLocation(fileChooser.getSelectedFile().toPath(), Path.of(savePath));

        return new RecipePhotoFact(newPath);
      } catch (IOException ex) {
        errorMessages(null, BUNDLES.errorMessage("MSG_PHOTO_IO_ERROR"));
        logger.error("Could not handle file " + fileChooser.getSelectedFile().toString() + ": ", ex);
      }
    }
    return inputFact;
  }

  public static Path copyFileToNewLocation(Path originalPath, Path savePath) throws IOException {
    var fileName = originalPath.getFileName();
    if (fileName == null) {
      throw new IOException("Filename empty.");
    }
    if (!Files.exists(savePath)) {
      Files.createDirectory(savePath);
    }
    Path copied = Paths.get(savePath.toString(), fileName.toString());
    Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    return copied;
  }
}
