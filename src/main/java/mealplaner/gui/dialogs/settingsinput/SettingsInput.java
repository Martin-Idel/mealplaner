// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.commons.gui.dialogs.DialogWindow.window;

import java.awt.event.ActionListener;
import java.util.Optional;

import javax.swing.JFrame;

import mealplaner.commons.gui.dialogs.DialogWindow;
import mealplaner.model.settings.Settings;

class SettingsInput {
  final DialogWindow dialogWindow;
  private Optional<Settings[]> enteredSettings = Optional.empty();

  SettingsInput(JFrame parentFrame, String label, String swingName) {
    dialogWindow = window(parentFrame, label, swingName);
  }

  Optional<Settings[]> getEnteredSettings() {
    return enteredSettings;
  }

  ActionListener getSaveListener(SettingTable settingTable) {
    return action -> {
      enteredSettings = Optional.of(settingTable.getSettings());
      dialogWindow.dispose();
    };
  }

  void adjustPanesTo() {
    dialogWindow.arrangeWithSize(550, 210);
  }
}