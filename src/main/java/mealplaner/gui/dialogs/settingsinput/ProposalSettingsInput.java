package mealplaner.gui.dialogs.settingsinput;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.model.settings.Settings.createSettings;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class ProposalSettingsInput extends SettingsInput {
  private final JFrame parentFrame;
  private SettingTable settingTable;

  public ProposalSettingsInput(JFrame parentFrame) {
    super(parentFrame, BUNDLES.message("settingsUpdateProposeTitle"));
    this.parentFrame = parentFrame;
  }

  public Optional<Settings[]> showDialog(DefaultSettings settings, ProposalOutline outline) {
    setup(settings, outline);
    dialogWindow.setVisible();
    return getEnteredSettings();
  }

  private List<Settings> createSettingsForTable(int numberOfDays) {
    Settings[] settings = new Settings[numberOfDays];
    Arrays.fill(settings, createSettings());
    return Arrays.asList(settings);
  }

  private void setup(DefaultSettings defaultSettings, ProposalOutline outline) {
    List<Settings> tableSettings = createSettingsForTable(outline.getNumberOfDays());
    LocalDate date = outline.isIncludedToday() ? outline.getDateToday()
        : outline.getDateToday().plusDays(1);
    settingTable = new SettingTable(tableSettings, date);

    JPanel buttonPanel = createButtonPanel(defaultSettings);

    settingTable.addJScrollTableToDialogCentre(dialogWindow);
    dialogWindow.addSouth(buttonPanel);
    adjustPanesTo(parentFrame);
  }

  private JPanel createButtonPanel(DefaultSettings defaultSettings) {
    return new ButtonPanelBuilder()
        .addButton(BUNDLES.message("useDefaultButton"),
            BUNDLES.message("useDefaultButtonMnemonic"),
            action -> settingTable.useDefaultSettings(defaultSettings))
        .addCancelDialogButton(dialogWindow)
        .addOkButton(getSaveListener(settingTable))
        .build();
  }
}
