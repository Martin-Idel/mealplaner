// SPDX-License-Identifier: MIT

package mealplaner.plugins.recipephoto.mealedit;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.errorMessages;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.dialogs.DialogWindow.window;
import static mealplaner.plugins.recipephoto.mealedit.RecipePhotoEdit.copyAndSaveImage;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.commons.gui.dialogs.DialogWindow;

public class ShowPhotoDialog {
  private static final Logger logger = LoggerFactory.getLogger(ShowPhotoDialog.class);
  private static final String SAVE_PATH = System.getProperty("user.home")
      + File.separator + ".mealplaner" + File.separator + "photos";

  private final DialogWindow dialogWindow;
  private RecipePhotoFact enteredRecipePhoto;
  private JLabel imageLabel;

  private ShowPhotoDialog(JFrame parentFrame) {
    dialogWindow = window(parentFrame, BUNDLES.message("recipePhotoInputDialogTitle"),
        "RecipePhotoInputDialog");
  }

  public static ShowPhotoDialog recipePhotoInput(JFrame parentFrame) {
    return new ShowPhotoDialog(parentFrame);
  }

  public RecipePhotoFact showDialog(RecipePhotoFact recipePhotoFact) {
    enteredRecipePhoto = recipePhotoFact;
    display(recipePhotoFact);
    dialogWindow.dispose();
    return enteredRecipePhoto;
  }

  private void display(RecipePhotoFact recipePhoto) {
    imageLabel = new JLabel();
    var gridPanel = GridPanel.gridPanel(1,1);
    gridPanel.add(imageLabel);
    dialogWindow.addCentral(gridPanel);

    ButtonPanel buttonPanel = displayButtons();

    dialogWindow.addSouth(buttonPanel);
    setPhoto(recipePhoto);
    dialogWindow.setVisible();
  }

  private ButtonPanel displayButtons() {
    return builder("RecipeInput")
        .addButton(
            BUNDLES.message("editRecipePhotoButton"),
            BUNDLES.message("editRecipePhotoButtonMnemonic"),
            getChangeListener())
        .addOkButton(pressed -> dialogWindow.dispose())
        .build();
  }

  private ActionListener getChangeListener() {
    return pressed -> {
      enteredRecipePhoto = copyAndSaveImage(enteredRecipePhoto, SAVE_PATH);
      setPhoto(enteredRecipePhoto);
      dialogWindow.repaint();
    };
  }

  private void setPhoto(RecipePhotoFact recipePhoto) {
    int width = 300;
    int height = 200;
    if (recipePhoto.getPath().isPresent()) {
      try {
        BufferedImage img = ImageIO.read(recipePhoto.getPath().get().toFile());
        width = 1000;
        height = width / img.getWidth() * img.getHeight();
        imageLabel.setIcon(new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
      } catch (IOException e) {
        errorMessages(null, BUNDLES.errorMessage("MSG_PHOTO_IO_ERROR"));
        logger.error("Could not load image from path: {}", recipePhoto.getPath());
      }
    }
    dialogWindow.arrangeWithSize(width, height);
  }
}
