// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

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
import javax.swing.SwingUtilities;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.builtins.courses.CourseTypeSetting;
import mealplaner.plugins.cookingtime.mealextension.CookingTime;
import mealplaner.plugins.cookingtime.settingextension.CookingTimeSubSetting;
import mealplaner.plugins.preference.settingextension.CookingPreferenceSubSetting;
import mealplaner.plugins.utensil.settingextension.CasseroleSubSetting;

public class ProposalSummaryPageObjectNative {
  private static final int NUMBER_OF_DEFAULT_SETTINGS_COLUMNS = 9;
  private static final int NUMBER_OF_WEEKDAYS = 7;

  private final JFrame frame;

  ProposalSummaryPageObjectNative(JFrame frame) {
    this.frame = frame;
  }

  public ProposalSummaryPageObjectNative proposalTabbedPane() throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      javax.swing.JTabbedPane tabbedPane = findFirstComponentOfClass(frame, javax.swing.JTabbedPane.class);
      tabbedPane.setSelectedIndex(TabbedPanes.PROPOSAL_SUMMARY.number());
    });
    return this;
  }

  public ProposalSummaryPageObjectNative updateCookedLast() throws Exception {
    SwingUtilities.invokeLater(() -> {
      JButton updateButton = (JButton) findComponentByName(frame, "ButtonProposalSummaryUpdate");
      updateButton.doClick();
    });
    Thread.sleep(1500);
    SwingUtilities.invokeAndWait(() -> {
      JButton confirmButton = findButtonInDialog("ButtonPanelUpdatePastMeals0");
      confirmButton.doClick();
    });
    Thread.sleep(1500);
    return this;
  }

  public ProposalSummaryPageObjectNative enterNumberOfDaysForProposal(NonnegativeInteger days) throws Exception {
    SwingUtilities.invokeLater(() -> {
      JTextField daysField = (JTextField) findComponentByName(frame, "InputFieldNonnegativeIntegerNumberDays");
      daysField.setText(Integer.toString(days.value));
    });
    Thread.sleep(100);
    return this;
  }

  public ProposalSummaryPageObjectNative enterDefaultSettings(DefaultSettings defaultSettings) throws Exception {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettingsMap();
    openDefaultSettingsDialog();
    JTable settingsTable = findTableInSettingsDialog();
    for (DayOfWeek day : DayOfWeek.values()) {
      enterSettingInTable(settingsTable, settings.get(day), day.getValue() - 1, false);
    }
    closeDefaultSettingsDialog();
    Thread.sleep(500);
    return this;
  }

  public ProposalSummaryPageObjectNative compareDefaultSettings(Map<DayOfWeek, Settings> originalSettingsMap) throws Exception {
    openDefaultSettingsDialog();
    Thread.sleep(500);
    JTable settingsTable = findTableInSettingsDialog();
    SwingUtilities.invokeAndWait(() -> {
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

  public ProposalTablePageObjectNative proposeWithSettings(Settings... settings) throws Exception {
    SwingUtilities.invokeLater(() -> {
      JButton proposalButton = (JButton) findComponentByName(frame, "ButtonProposalSummaryMakeProposal");
      assertThat(proposalButton.isEnabled()).isTrue();
      proposalButton.doClick();
    });
    Thread.sleep(1500);
    JTable proposalSettingsTable = findTableInProposalSettingsDialog();
    for (int i = 0; i < settings.length; i++) {
      enterSettingInTable(proposalSettingsTable, settings[i], i, true);
    }
    SwingUtilities.invokeLater(() -> {
      JButton confirmButton = findButtonInDialog("ButtonPanelProposalSettingsInput2");
      confirmButton.doClick();
    });
    Thread.sleep(1500);
    return new ProposalTablePageObjectNative(frame);
  }

  private void openDefaultSettingsDialog() throws Exception {
    SwingUtilities.invokeLater(() -> {
      JButton defaultSettingsButton = (JButton) findComponentByName(frame, "ButtonProposalSummaryDefaultSettings");
      defaultSettingsButton.doClick();
    });
    Thread.sleep(1500);
  }

  private void closeDefaultSettingsDialog() throws Exception {
    SwingUtilities.invokeLater(() -> {
      JButton saveButton = findButtonInDialog("ButtonPanelDefaultSettingsInput0");
      saveButton.doClick();
    });
    Thread.sleep(1500);
  }

  private JTable findTableInSettingsDialog() throws Exception {
    final JTable[] tableRef = new JTable[1];
    SwingUtilities.invokeAndWait(() -> {
      JDialog dialog = findDialog();
      JTable namedTable = (JTable) findComponentByName(dialog, "DefaultSettingsTable");
      if (namedTable != null) {
        tableRef[0] = namedTable;
      } else {
        tableRef[0] = findFirstComponentOfClass(dialog, JTable.class);
      }
    });
    return tableRef[0];
  }

  private JTable findTableInProposalSettingsDialog() throws Exception {
    final JTable[] tableRef = new JTable[1];
    SwingUtilities.invokeAndWait(() -> {
      JDialog dialog = findDialog();
      JTable namedTable = (JTable) findComponentByName(dialog, "ProposalSettingsTable");
      if (namedTable != null) {
        tableRef[0] = namedTable;
      } else {
        tableRef[0] = findFirstComponentOfClass(dialog, JTable.class);
      }
    });
    return tableRef[0];
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
      SwingUtilities.invokeLater(() -> {
        Object newValue = !cookingTime.contains(time);
        settingsTable.setValueAt(newValue, row, column);
      });
      Thread.sleep(50);
    }
  }

private void updateComboBox(JTable settingsTable,
                               int row,
                               int column,
                               Object value) throws Exception {
    SwingUtilities.invokeAndWait(() -> {
      Object currentValue = settingsTable.getValueAt(row, column);
      if (currentValue == null || !currentValue.equals(value)) {
        settingsTable.setValueAt(value, row, column);
      }
    });
    Thread.sleep(50);
  }

  private boolean checkBoxNeedsUpdate(JTable settingsTable,
                                      int row,
                                      int column,
                                      CookingTimeSubSetting cookingTime,
                                      CookingTime time) throws Exception {
    final boolean[] result = new boolean[1];
    SwingUtilities.invokeAndWait(() -> {
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

  private Object[][] defaultSettingsTableEntries(DefaultSettings defaultSettings) {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettingsMap();
    Object[][] content = new Object[NUMBER_OF_WEEKDAYS][NUMBER_OF_DEFAULT_SETTINGS_COLUMNS];
    for (DayOfWeek day : DayOfWeek.values()) {
      Settings setting = settings.get(day);
      int row = day.getValue() - 1;
      content[row][0] = day.getDisplayName(FULL, BUNDLES.locale());
      content[row][1] = !setting.getTypedSubSetting(CookingTimeSubSetting.class).contains(VERY_SHORT);
      content[row][2] = !setting.getTypedSubSetting(CookingTimeSubSetting.class).contains(SHORT);
      content[row][3] = !setting.getTypedSubSetting(CookingTimeSubSetting.class).contains(MEDIUM);
      content[row][4] = !setting.getTypedSubSetting(CookingTimeSubSetting.class).contains(LONG);
      content[row][5] = setting.getNumberOfPeople();
      content[row][6] = setting.getTypedSubSetting(CasseroleSubSetting.class).getCasseroleSettings();
      content[row][7] = setting.getTypedSubSetting(CookingPreferenceSubSetting.class).getPreferences();
      content[row][8] = setting.getTypedSubSetting(CourseTypeSetting.class).getCourseSetting();
    }
    return content;
  }

  private JButton findButtonInDialog(String buttonName) {
    JDialog dialog = findDialog();
    return (JButton) findComponentByName(dialog, buttonName);
  }

  private JDialog findDialog() {
    for (java.awt.Window window : java.awt.Window.getWindows()) {
      if (window instanceof JDialog && window.isVisible()) {
        return (JDialog) window;
      }
    }
    return null;
  }

  private java.awt.Component findComponentByName(java.awt.Container parent, String name) {
    if (name.equals(parent.getName())) {
      return parent;
    }
    for (java.awt.Component component : parent.getComponents()) {
      if (name.equals(component.getName())) {
        return component;
      }
      if (component instanceof java.awt.Container) {
        java.awt.Component found = findComponentByName((java.awt.Container) component, name);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }

  private <T> T findFirstComponentOfClass(java.awt.Container parent, Class<T> clazz) {
    if (clazz.isInstance(parent)) {
      return clazz.cast(parent);
    }
    for (java.awt.Component component : parent.getComponents()) {
      if (clazz.isInstance(component)) {
        return clazz.cast(component);
      }
      if (component instanceof java.awt.Container) {
        T found = findFirstComponentOfClass((java.awt.Container) component, clazz);
        if (found != null) {
          return found;
        }
      }
    }
    return null;
  }
}