// SPDX-License-Identifier: MIT

package guitests.pageobjects;

import static guitests.pageobjects.TabbedPanes.PROPOSAL_SUMMARY;
import static java.time.format.TextStyle.FULL;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.model.meal.enums.CookingTime.LONG;
import static mealplaner.model.meal.enums.CookingTime.MEDIUM;
import static mealplaner.model.meal.enums.CookingTime.SHORT;
import static mealplaner.model.meal.enums.CookingTime.VERY_SHORT;
import static org.assertj.swing.core.MouseButton.LEFT_BUTTON;
import static org.assertj.swing.data.TableCell.row;

import java.time.DayOfWeek;
import java.util.Map;
import java.util.function.Consumer;

import org.assertj.swing.data.TableCell;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTableFixture;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.model.meal.enums.CookingTime;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.model.settings.subsettings.CookingTimeSetting;

public class ProposalSummaryPageObject {
  private static final int NUMBER_OF_DEFAULT_SETTINGS_COLUMNS = 9;
  private static final int NUMBER_OF_WEEKDAYS = 7;

  private final FrameFixture window;

  ProposalSummaryPageObject(FrameFixture window) {
    this.window = window;
  }

  public ProposalSummaryPageObject proposalTabbedPane() {
    window.tabbedPane().selectTab(PROPOSAL_SUMMARY.number());
    return this;
  }

  public ProposalSummaryPageObject updateCookedLast() {
    window.button("ButtonProposalSummaryUpdate").click();
    window.dialog().button("ButtonPanelUpdatePastMeals0").click();
    return this;
  }

  public ProposalSummaryPageObject enterNumberOfDaysForProposal(NonnegativeInteger days) {
    window.textBox("InputFieldNonnegativeIntegerNumberDays")
        .enterText(Integer.toString(days.value));
    return this;
  }

  public ProposalSummaryPageObject enterDefaultSettings(DefaultSettings defaultSettings) {
    inDefaultSettingsDialog(settingsTable -> enterDefaultSettings(defaultSettings, settingsTable));
    return this;
  }

  private void enterDefaultSettings(DefaultSettings defaultSettings, JTableFixture settingsTable) {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettings();
    for (DayOfWeek day : DayOfWeek.values()) {
      enterSettingInDialog(settingsTable, settings.get(day), day.getValue() - 1, false);
    }
  }

  public ProposalSummaryPageObject compareDefaultSettings(DefaultSettings defaultSettings) {
    inDefaultSettingsDialog(settingsTable -> settingsTable
        .requireContents(defaultSettingsTableEntries(defaultSettings)));
    return this;
  }

  public ProposalTablePageObject proposeWithSettings(Settings... settings) {
    proposalButton().requireEnabled();
    proposalButton().click();
    for (int i = 0; i < settings.length; i++) {
      enterSetting(window.dialog().table(), settings[i], i);
    }
    window.dialog().button("ButtonPanelProposalSettingsInput2").click();
    return new ProposalTablePageObject(window);
  }

  public JButtonFixture proposalButton() {
    return window.button("ButtonProposalSummaryMakeProposal");
  }

  private ProposalSummaryPageObject enterSetting(JTableFixture settingsTable, Settings setting,
      int dayNumber) {
    enterSettingInDialog(settingsTable, setting, dayNumber, true);
    return this;
  }

  private void inDefaultSettingsDialog(Consumer<JTableFixture> doInSettingsTable) {
    defaultSettingsButton().click();
    DialogFixture settingsDialog = window.dialog();
    JTableFixture settingsTable = settingsDialog.table();
    settingsTable.requireRowCount(NUMBER_OF_WEEKDAYS);
    settingsTable.requireColumnCount(NUMBER_OF_DEFAULT_SETTINGS_COLUMNS);
    doInSettingsTable.accept(settingsTable);
    settingsDialog.button("ButtonPanelDefaultSettingsInput1");
  }

  private JButtonFixture defaultSettingsButton() {
    return window.button("ButtonProposalSummaryDefaultSettings");
  }

  private void enterSettingInDialog(JTableFixture settingsTable, Settings setting, int dayNumber,
      boolean proposal) {
    int firstColumn = proposal ? 2 : 1;
    CookingTimeSetting timeSetting = setting.getCookingTime();
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, VERY_SHORT);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, SHORT);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, MEDIUM);
    updateCheckBox(settingsTable, row(dayNumber).column(firstColumn++), timeSetting, LONG);
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getNumberOfPeople().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getCasserole().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getPreference().toString());
    updateComboBox(settingsTable, row(dayNumber).column(firstColumn++),
        setting.getCourseSettings().toString());
  }

  private void updateCheckBox(JTableFixture settingsTable,
      TableCell checkbox,
      CookingTimeSetting cookingTime,
      CookingTime time) {
    if (checkBoxNeedsUpdate(settingsTable, checkbox, cookingTime, time)) {
      settingsTable.click(checkbox, LEFT_BUTTON);
    }
  }

  private void updateComboBox(JTableFixture settingsTable,
      TableCell cell,
      String value) {
    if (!settingsTable.cell(cell).value().equals(value)) {
      settingsTable.enterValue(cell, value);
    }
  }

  private boolean checkBoxNeedsUpdate(JTableFixture settingsTable,
      TableCell checkbox,
      CookingTimeSetting cookingTime,
      CookingTime time) {
    return cookingTime.contains(time) == settingsTable.valueAt(checkbox)
            .equals(Boolean.toString(true));
  }

  private String[][] defaultSettingsTableEntries(DefaultSettings defaultSettings) {
    Map<DayOfWeek, Settings> settings = defaultSettings.getDefaultSettings();
    String[][] content = new String[NUMBER_OF_WEEKDAYS][NUMBER_OF_DEFAULT_SETTINGS_COLUMNS];
    for (DayOfWeek day : DayOfWeek.values()) {
      Settings setting = settings.get(day);
      int row = day.getValue() - 1;
      content[row][0] = day.getDisplayName(FULL, BUNDLES.locale());
      content[row][1] = Boolean.toString(!setting.getCookingTime().contains(VERY_SHORT));
      content[row][2] = Boolean.toString(!setting.getCookingTime().contains(SHORT));
      content[row][3] = Boolean.toString(!setting.getCookingTime().contains(MEDIUM));
      content[row][4] = Boolean.toString(!setting.getCookingTime().contains(LONG));
      content[row][5] = setting.getNumberOfPeople().toString();
      content[row][6] = setting.getCasserole().toString();
      content[row][7] = setting.getPreference().toString();
      content[row][8] = setting.getCourseSettings().toString();
    }
    return content;
  }
}
