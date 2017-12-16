package mealplaner.gui.dialogs.settingsinput;

import static java.time.DayOfWeek.MONDAY;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.ButtonPanelBuilder.justDisposeListener;
import static mealplaner.model.settings.DefaultSettings.from;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.commons.gui.ButtonPanelBuilder;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class DefaultSettingsInput extends SettingsInput {
  private final JFrame parentFrame;

  public DefaultSettingsInput(JFrame parentFrame) {
    super(parentFrame, BUNDLES.message("settingsUpdateDefaultTitle"));
    this.parentFrame = parentFrame;
  }

  public Optional<DefaultSettings> showDialog(DefaultSettings settings) {
    setup(settings);
    dialogWindow.setVisible();
    Optional<Settings[]> changedSettings = getEnteredSettings();
    return changedSettings.isPresent() ? transformToDefault(changedSettings.get()) : empty();
  }

  private void setup(DefaultSettings defaultSettings) {
    SettingTable settingTable = new SettingTable(defaultSettings);

    JPanel buttonPanel = createButtonPanel(settingTable);

    settingTable.addJScrollTableToDialogCentre(dialogWindow);
    dialogWindow.addSouth(buttonPanel);
    adjustPanesTo(parentFrame);
  }

  private JPanel createButtonPanel(SettingTable settingTable) {
    return new ButtonPanelBuilder()
        .addSaveButton(getSaveListener(settingTable))
        .addExitButton(justDisposeListener(dialogWindow))
        .build();
  }

  private Optional<DefaultSettings> transformToDefault(Settings... settings) {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    for (int i = 0; i < settings.length; i++) {
      defaultSettings.put(MONDAY.plus(i), settings[i]);
    }
    return of(from(defaultSettings));
  }
}
