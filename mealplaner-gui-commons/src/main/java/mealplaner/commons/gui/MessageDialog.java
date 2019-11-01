// SPDX-License-Identifier: MIT

package mealplaner.commons.gui;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static mealplaner.commons.BundleStore.BUNDLES;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public final class MessageDialog {
  private MessageDialog() {
  }

  public static void errorMessages(JFrame frame, String errorMessage) {
    JOptionPane.showMessageDialog(frame, errorMessage,
        BUNDLES.errorMessage("ERR_HEADING"), ERROR_MESSAGE);
  }

  public static void userErrorMessage(JFrame frame, String informationMessage) {
    JOptionPane.showMessageDialog(frame, informationMessage,
        BUNDLES.message("errorHeading"), JOptionPane.INFORMATION_MESSAGE);
  }

  public static void showSaveExitDialog(JFrame frame, String saveMessage, Runnable saveAction) {
    int result = JOptionPane.showConfirmDialog(frame, saveMessage);
    if (result == JOptionPane.YES_OPTION) {
      saveAction.run();
      frame.dispose();
    } else if (result == JOptionPane.NO_OPTION) {
      frame.dispose();
    }
  }
}
