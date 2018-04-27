// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.proposal;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.LONG;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.SEVEN;
import static mealplaner.commons.gui.GridPanel.gridPanel;
import static mealplaner.commons.gui.SwingUtilityMethods.createButton;

import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.GridPanel;
import mealplaner.commons.gui.inputfields.CheckboxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonnegativeIntegerInputField;
import mealplaner.model.DataStore;
import mealplaner.model.DataStoreEventType;
import mealplaner.model.DataStoreListener;
import mealplaner.model.proposal.ProposalOutline;
import mealplaner.model.proposal.ProposalOutline.ProposalOutlineBuilder;

public class ProposalSummary implements DataStoreListener {
  private final DataStore mealPlan;
  private final GridPanel dataPanel;
  private JLabel dateShow;
  private JButton dateUpdate;
  private InputField<NonnegativeInteger> numberOfDaysField;
  private InputField<Boolean> takeTodayCheckBox;
  private InputField<Boolean> randomiseCheckBox;
  private InputField<Boolean> takeDefaultCheckBox;
  private JButton defaultSettings;
  private JButton giveProposal;

  public ProposalSummary(DataStore mealPlan) {
    this.mealPlan = mealPlan;
    this.dataPanel = gridPanel(0, 2);

    mealPlan.register(this);
  }

  public GridPanel buildProposalPanel(ActionListener updateMeals, ActionListener setDefaultSettings,
      ActionListener makeProposal) {
    buildDateShowField(mealPlan.getTime());
    buildInputFields();
    buildButtons(updateMeals, setDefaultSettings, makeProposal);

    adjustFieldsOnPanel();

    boolean lastDayWasToday = mealPlan.getDaysPassed() == 0;
    dateUpdate.setEnabled(!lastDayWasToday);
    giveProposal.setEnabled(lastDayWasToday);

    return dataPanel;
  }

  private void buildDateShowField(LocalDate lastDate) {
    String formattedLastDate = lastDate
        .format(ofLocalizedDate(LONG).withLocale(BUNDLES.locale()));
    dateShow = new JLabel(BUNDLES.message("updatedLastDate") + " " + formattedLastDate);
  }

  private void buildButtons(ActionListener updateMeals,
      ActionListener setDefaultSettings, ActionListener makeProposal) {
    dateUpdate = createButton("ButtonProposalSummaryUpdate",
        BUNDLES.message("updateButton"),
        BUNDLES.message("updateButtonMnemonic"),
        action -> {
          updateMeals.actionPerformed(action);
          update();
        });
    defaultSettings = createButton("ButtonProposalSummaryDefaultSettings",
        BUNDLES.message("proposalChangeDefaultSettingsButton"),
        BUNDLES.message("proposalChangeDefaultSettingsButtonMnemonic"),
        setDefaultSettings);
    giveProposal = createButton("ButtonProposalSummaryMakeProposal",
        BUNDLES.message("proposalShowButton"),
        BUNDLES.message("proposalShowButtonMnemonic"), makeProposal);
  }

  private void buildInputFields() {
    numberOfDaysField = new NonnegativeIntegerInputField(
        BUNDLES.message("proposalNumberOfDays"), "NumberDays", SEVEN);
    takeTodayCheckBox = new CheckboxInputField(BUNDLES.message("proposalStartToday"), "TakeToday");
    randomiseCheckBox = new CheckboxInputField(BUNDLES.message("proposalRandomize"), "Randomize");
    takeDefaultCheckBox = new CheckboxInputField(
        BUNDLES.message("proposalApplyDefaultSettings"), "ApplyDefault");
  }

  private void adjustFieldsOnPanel() {
    dataPanel.add(dateShow);
    dataPanel.add(dateUpdate);
    numberOfDaysField.addToPanel(dataPanel);
    takeTodayCheckBox.addToPanel(dataPanel);
    randomiseCheckBox.addToPanel(dataPanel);
    takeDefaultCheckBox.addToPanel(dataPanel);
    dataPanel.add(defaultSettings);
    dataPanel.add(giveProposal);
  }

  public ProposalOutline getProposalOutline() {
    ProposalOutlineBuilder builder = ProposalOutline.ProposalOutlineBuilder
        .of(numberOfDaysField.getUserInput().value, mealPlan.getTime());
    if (randomiseCheckBox.getUserInput()) {
      builder.randomise();
    }
    if (takeTodayCheckBox.getUserInput()) {
      builder.includeToday();
    }
    if (takeDefaultCheckBox.getUserInput()) {
      builder.takeDefaultSettings();
    }

    return builder.build();
  }

  public void update() {
    String formattedLastDate = mealPlan.getTime()
        .format(ofLocalizedDate(LONG).withLocale(BUNDLES.locale()));
    dateShow.setText(BUNDLES.message("updatedLastDate") + " " + formattedLastDate);

    boolean lastDayWasToday = mealPlan.getDaysPassed() == 0;
    dateUpdate.setEnabled(!lastDayWasToday);
    giveProposal.setEnabled(lastDayWasToday);
  }

  @Override
  public void updateData(DataStoreEventType event) {
    if (event == DataStoreEventType.DATE_UPDATED) {
      update();
    }
  }
}