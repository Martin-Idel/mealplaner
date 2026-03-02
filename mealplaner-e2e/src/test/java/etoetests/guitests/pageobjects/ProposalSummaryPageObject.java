// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

import static etoetests.guitests.constants.ComponentNames.BUTTON_UPDATEPASTMEALS_CONFIRM;
import static etoetests.guitests.constants.ComponentNames.BUTTON_PROPOSALSETTINGS_CONFIRM;
import static etoetests.guitests.constants.ComponentNames.BUTTON_DEFAULTSETTINGS_SAVE;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.LONG;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.MEDIUM;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.SHORT;
import static mealplaner.plugins.cookingtime.mealextension.CookingTime.VERY_SHORT;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JTextField;

import etoetests.guitests.helpers.SwingTestHelper;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import mealplaner.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.plugins.utensil.settingextension.CasseroleSubSetting;

public class ProposalSummaryPageObject extends BasePageObject {
  private static final int NUMBER_OF_DEFAULT_SETTINGS_COLUMNS = 9;
  private static final int NUMBER_OF_WEEKDAYS = 7;

  ProposalSummaryPageObject(JFrame frame) {
    super(frame);
  }

  public ProposalSummaryPageObject proposalTabbedPane() throws Exception {
    try {
      javax.swing.JTabbedPane tabbedPane = helper.findFirstComponentOfClass(frame, javax.swing.JTabbedPane.class);
      tabbedPane.setSelectedIndex(TabbedPanes.PROPOSAL_SUMMARY.number());
      return this;
    } catch (Exception e) {
      throw new RuntimeException("Failed to set proposal tabbed pane", e);
    }
  }

  public ProposalSummaryPageObject updateCookedLast() throws Exception {
    JButton updateButton = helper.findComponentByName(frame, "ButtonProposalSummaryUpdate");
    helper.clickButtonOnEdt(updateButton);
    JDialog dialog = helper.findDialogContaining(BUTTON_UPDATEPASTMEALS_CONFIRM, 1500);
    JButton confirmButton = helper.findComponentByName(dialog, BUTTON_UPDATEPASTMEALS_CONFIRM);
    helper.clickButtonOnEdt(confirmButton);
    helper.waitForCondition(() -> {
      return helper.findDialog() == null;
    }, 3000, "Dialog did not close within timeout");
    return this;
  }

  public ProposalSummaryPageObject enterNumberOfDaysForProposal(NonnegativeInteger days) throws Exception {
    JTextField daysField = helper.findComponentByName(frame, "InputFieldNonnegativeIntegerNumberDays");
    helper.invokeLaterVoid(() -> {
      daysField.setText(Integer.toString(days.value));
    });
    return this;
  }

  public ProposalSummaryPageObject enterDefaultSettings(DefaultSettings defaultSettings) throws Exception {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettingsMap();
    openDefaultSettingsDialog();
    JTable settingsTable = findTableInSettingsDialog();
    for (DayOfWeek day : DayOfWeek.values()) {
      enterSettingInTable(settingsTable, settings.get(day), day.getValue() - 1, false);
    }
    closeDefaultSettingsDialog();
    return this;
  }

  public ProposalSummaryPageObject compareDefaultSettings(Map<DayOfWeek, Settings> originalSettingsMap) throws Exception {
    openDefaultSettingsDialog();
    JTable settingsTable = findTableInSettingsDialog();
    helper.invokeAndWaitVoid(() -> {
      assertThat(settingsTable.getRowCount()).isEqualTo(NUMBER_OF_WEEKDAYS);
      assertThat(settingsTable.getColumnCount()).isEqualTo(NUMBER_OF_DEFAULT_SETTINGS_COLUMNS);
      for (Map.Entry<DayOfWeek, Settings> entry : originalSettingsMap.entrySet()) {
        int row = entry.getKey().getValue() - 1;
        Settings expectedSettings = entry.getValue();
        for (int col = 1; col < NUMBER_OF_DEFAULT_SETTINGS_COLUMNS; col++) {
          Object actualValue = settingsTable.getValueAt(row, col);
          Object expectedValue = getExpectedValueForRowColumn(expectedSettings, col);
          assertThat(actualValue.toString()).isEqualTo(expectedValue.toString());
        }
      }
    });
    closeDefaultSettingsDialog();
    return this;
  }

  public ProposalTablePageObject proposeWithSettings(Settings... settings) throws Exception {
    JButton proposalButton = helper.findComponentByName(frame, "ButtonProposalSummaryMakeProposal");
    helper.invokeLaterVoid(() -> {
      assertThat(proposalButton.isEnabled()).isTrue();
      proposalButton.doClick();
    });
    JDialog dialog = helper.findDialogContaining(BUTTON_PROPOSALSETTINGS_CONFIRM, 3000);
    JTable proposalSettingsTable = findTableInProposalSettingsDialog(dialog);
    for (int i = 0; i < settings.length; i++) {
      enterSettingInTable(proposalSettingsTable, settings[i], i, true);
    }
    JButton confirmButton = helper.findComponentByName(dialog, BUTTON_PROPOSALSETTINGS_CONFIRM);
    helper.clickButtonOnEdt(confirmButton);
    helper.waitForDialogToClose(dialog, 3000);
    return new ProposalTablePageObject(frame);
  }

  private void openDefaultSettingsDialog() throws Exception {
    JButton defaultSettingsButton = helper.findComponentByName(frame, "ButtonProposalSummaryDefaultSettings");
    helper.clickButtonOnEdt(defaultSettingsButton);
    helper.findDialogContaining(BUTTON_DEFAULTSETTINGS_SAVE, 1500);
  }

  private void closeDefaultSettingsDialog() throws Exception {
    JDialog dialog = helper.findDialogContaining(BUTTON_DEFAULTSETTINGS_SAVE, 1500);
    JButton saveButton = helper.findComponentByName(dialog, BUTTON_DEFAULTSETTINGS_SAVE);
    helper.clickButtonOnEdt(saveButton);
    helper.waitForCondition(() -> {
      return helper.findDialog() == null;
    }, 3000, "Dialog did not close within timeout");
  }

  private JTable findTableInSettingsDialog() throws Exception {
    JDialog dialog = helper.findDialogContaining(BUTTON_DEFAULTSETTINGS_SAVE, 3000);
    return findNamedTableInDialogOrFallback(dialog, "DefaultSettingsTable");
  }

  private JTable findTableInProposalSettingsDialog(JDialog dialog) throws Exception {
    return findNamedTableInDialogOrFallback(dialog, "ProposalSettingsTable");
  }

  private void enterSettingInTable(JTable settingsTable, Settings setting, int dayNumber,
                                   boolean proposal) throws Exception {
    int firstColumn = proposal ? 2 : 1;
    CookingTimeSubSetting timeSetting = setting.getTypedSubSetting(CookingTimeSubSetting.class);
    updateCheckBox(settingsTable, dayNumber, firstColumn++, timeSetting, VERY_SHORT);
    updateCheckBox(settingsTable, dayNumber, firstColumn++, timeSetting, SHORT);
    updateCheckBox(settingsTable, dayNumber, firstColumn++, timeSetting, MEDIUM);
    updateCheckBox(settingsTable, dayNumber, firstColumn++, timeSetting, LONG);
    updateComboBox(settingsTable, dayNumber, firstColumn++,
        setting.getNumberOfPeople());
    updateComboBox(settingsTable, dayNumber, firstColumn++,
        setting.getTypedSubSetting(CasseroleSubSetting.class).getCasseroleSettings());
    updateComboBox(settingsTable, dayNumber, firstColumn++,
        setting.getTypedSubSetting(CookingPreferenceSubSetting.class).getPreferences());
    updateComboBox(settingsTable, dayNumber, firstColumn,
        setting.getTypedSubSetting(CourseTypeSetting.class).getCourseSetting());
  }

private void updateCheckBox(JTable settingsTable,
                                int row,
                                int column,
                                CookingTimeSubSetting cookingTime,
                                CookingTime time) throws Exception {
    if (checkBoxNeedsUpdate(settingsTable, row, column, cookingTime, time)) {
      helper.invokeLaterVoid(() -> {
        Object newValue = !cookingTime.contains(time);
        settingsTable.setValueAt(newValue, row, column);
      });
    }
  }

 private void updateComboBox(JTable settingsTable,
                                 int row,
                                 int column,
                                 Object value) throws Exception {
    helper.invokeAndWaitVoid(() -> {
      Object currentValue = settingsTable.getValueAt(row, column);
      if (currentValue == null || !currentValue.equals(value)) {
        settingsTable.setValueAt(value, row, column);
      }
    });
  }

  private boolean checkBoxNeedsUpdate(JTable settingsTable,
                                      int row,
                                      int column,
                                      CookingTimeSubSetting cookingTime,
                                      CookingTime time) throws Exception {
    final boolean[] result = new boolean[1];
    helper.invokeAndWaitVoid(() -> {
      Object cellValue = settingsTable.getValueAt(row, column);
      boolean isChecked = cellValue instanceof Boolean && (Boolean) cellValue;
      result[0] = cookingTime.contains(time) == isChecked;
    });
    return result[0];
  }

  private Object getExpectedValueForRowColumn(Settings setting, int col) {
    int adjustedCol = col - 1;
    CookingTimeSubSetting timeSetting = setting.getTypedSubSetting(CookingTimeSubSetting.class);
    switch (adjustedCol) {
      case 0:
        return !timeSetting.contains(VERY_SHORT);
      case 1:
        return !timeSetting.contains(SHORT);
      case 2:
        return !timeSetting.contains(MEDIUM);
      case 3:
        return !timeSetting.contains(LONG);
      case 4:
        return setting.getNumberOfPeople();
      case 5:
        return setting.getTypedSubSetting(CasseroleSubSetting.class).getCasseroleSettings();
      case 6:
        return setting.getTypedSubSetting(CookingPreferenceSubSetting.class).getPreferences();
      case 7:
        return setting.getTypedSubSetting(CourseTypeSetting.class).getCourseSetting();
      default:
        return "";
    }
  }
}