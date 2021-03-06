// SPDX-License-Identifier: MIT

package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.model.settings.SettingsBuilder.defaultSetting;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.swing.JFrame;

import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.dialogs.DialogCreatingWithAdditional;
import mealplaner.model.DataStore;
import mealplaner.model.proposal.ProposalOutline;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;

public class ProposalSettingsInput extends SettingsInput
    implements DialogCreatingWithAdditional<ProposalOutline, Optional<Settings[]>> {
  private SettingTable settingTable;

  public ProposalSettingsInput(JFrame parentFrame) {
    super(parentFrame, BUNDLES.message("settingsUpdateProposeTitle"), "ProposalSettingsDialog");
  }

  @Override
  public Optional<Settings[]> showDialog(
      ProposalOutline outline, DataStore dataStore, PluginStore pluginStore) {
    setup(dataStore.getDefaultSettings(), outline, pluginStore);
    dialogWindow.setVisible();
    return getEnteredSettings();
  }

  private List<Settings> createSettingsForTable(int numberOfDays, PluginStore pluginStore) {
    Settings[] settings = new Settings[numberOfDays];
    Arrays.fill(settings, defaultSetting(pluginStore));
    return Arrays.asList(settings);
  }

  private void setup(
      DefaultSettings defaultSettings,
      ProposalOutline outline,
      PluginStore pluginStore) {
    List<Settings> tableSettings = createSettingsForTable(outline.getNumberOfDays(), pluginStore);
    LocalDate date = outline.isIncludedToday() ? outline.getDateToday()
        : outline.getDateToday().plusDays(1);
    settingTable = new SettingTable(tableSettings, date);

    ButtonPanel buttonPanel = createButtonPanel(defaultSettings);

    settingTable.addJScrollTableToDialogCentre(
        dialogWindow, pluginStore.getRegisteredSettingsInputGuiExtensions());
    dialogWindow.addSouth(buttonPanel);
    adjustPanesTo();
  }

  private ButtonPanel createButtonPanel(DefaultSettings defaultSettings) {
    return builder("ProposalSettingsInput")
        .addButton(BUNDLES.message("useDefaultButton"),
            BUNDLES.message("useDefaultButtonMnemonic"),
            action -> settingTable.useDefaultSettings(defaultSettings))
        .addCancelDialogButton(dialogWindow)
        .addOkButton(getSaveListener(settingTable))
        .build();
  }
}
