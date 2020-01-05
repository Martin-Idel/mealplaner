// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.settingsinput;

import static java.time.DayOfWeek.MONDAY;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.justDisposeListener;
import static mealplaner.model.settings.DefaultSettings.from;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.JFrame;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.dialogs.DialogCreating;
import mealplaner.model.DataStore;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.SettingsInputDialogExtension;

public class DefaultSettingsInput extends SettingsInput implements DialogCreating<Optional<DefaultSettings>> {

  public DefaultSettingsInput(JFrame parentFrame) {
    super(parentFrame, BUNDLES.message("settingsUpdateDefaultTitle"), "DefaultSettingsDialog");
  }

  @Override
  public Optional<DefaultSettings> showDialog(DataStore store, PluginStore pluginStore) {
    setup(store.getDefaultSettings(), pluginStore.getRegisteredSettingsInputGuiExtensions());
    dialogWindow.setVisible();
    Optional<Settings[]> changedSettings = getEnteredSettings();
    return changedSettings.flatMap(setting -> transformToDefault(pluginStore, setting));
  }

  private void setup(
      DefaultSettings defaultSettings,
      Collection<SettingsInputDialogExtension> settingsInputDialogExtensions) {
    SettingTable settingTable = new SettingTable(defaultSettings);

    ButtonPanel buttonPanel = createButtonPanel(settingTable);

    settingTable.addJScrollTableToDialogCentre(dialogWindow, settingsInputDialogExtensions);
    dialogWindow.addSouth(buttonPanel);
    adjustPanesTo();
  }

  private ButtonPanel createButtonPanel(SettingTable settingTable) {
    return builder("DefaultSettingsInput")
        .addSaveButton(getSaveListener(settingTable))
        .addExitButton(justDisposeListener(dialogWindow))
        .build();
  }

  private Optional<DefaultSettings> transformToDefault(PluginStore pluginStore, Settings... settings) {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    for (int i = 0; i < settings.length; i++) {
      defaultSettings.put(MONDAY.plus(i), settings[i]);
    }
    return of(from(defaultSettings, pluginStore));
  }
}
