package mealplaner.gui;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.LONG;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.NonnegativeInteger.SEVEN;
import static mealplaner.commons.gui.SwingUtilityMethods.createButton;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.commons.gui.SwingUtilityMethods;
import mealplaner.commons.gui.inputfields.CheckboxInputField;
import mealplaner.commons.gui.inputfields.InputField;
import mealplaner.commons.gui.inputfields.NonnegativeIntegerInputField;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.ProposalOutline.ProposalOutlineBuilder;

public class ProposalSummary implements DataStoreListener {
  private DataStore mealPlan;
  private JPanel dataPanel;
  private JLabel dateShow;
  private JButton dateUpdate;
  private InputField<NonnegativeInteger> numberOfDaysField;
  private InputField<Boolean> takeTodayCheckBox;
  private InputField<Boolean> randomiseCheckBox;
  private InputField<Boolean> takeDefaultCheckBox;
  private JButton defaultSettings;
  private JButton giveProposal;

  public ProposalSummary(DataStore mealPlan, JFrame parentFrame) {
    this.mealPlan = mealPlan;
    this.dataPanel = new JPanel();
    this.dataPanel.setLayout(new GridLayout(0, 2));

    mealPlan.register(this);
  }

  public JPanel buildProposalPanel(ActionListener updateMeals, ActionListener setDefaultSettings,
      ActionListener makeProposal) {
    buildDateShowField(mealPlan.getTime());
    buildInputFields();
    buildButtons(updateMeals, setDefaultSettings, makeProposal);

    adjustFieldsOnPanel();

    if (mealPlan.getDaysPassed() == 0) {
      dateUpdate.setEnabled(false);
      giveProposal.setEnabled(true);
    } else {
      dateUpdate.setEnabled(true);
      giveProposal.setEnabled(false);
    }

    return dataPanel;
  }

  private void buildDateShowField(LocalDate lastDate) {
    String formattedLastDate = lastDate
        .format(ofLocalizedDate(LONG).withLocale(BUNDLES.locale()));
    dateShow = new JLabel(BUNDLES.message("updatedLastDate") + " " + formattedLastDate);
  }

  private void buildButtons(ActionListener updateMeals,
      ActionListener setDefaultSettings, ActionListener makeProposal) {
    dateUpdate = createButton(BUNDLES.message("updateButton"),
        BUNDLES.message("updateButtonMnemonic"),
        action -> {
          updateMeals.actionPerformed(action);
          update();
        });
    defaultSettings = SwingUtilityMethods.createButton(
        BUNDLES.message("proposalChangeDefaultSettingsButton"),
        BUNDLES.message("proposalChangeDefaultSettingsButtonMnemonic"),
        setDefaultSettings);
    giveProposal = SwingUtilityMethods.createButton(BUNDLES.message("proposalShowButton"),
        BUNDLES.message("proposalShowButtonMnemonic"), makeProposal);
  }

  private void buildInputFields() {
    numberOfDaysField = new NonnegativeIntegerInputField(
        BUNDLES.message("proposalNumberOfDays"), SEVEN);
    takeTodayCheckBox = new CheckboxInputField(BUNDLES.message("proposalStartToday"));
    randomiseCheckBox = new CheckboxInputField(BUNDLES.message("proposalRandomize"));
    takeDefaultCheckBox = new CheckboxInputField(
        BUNDLES.message("proposalApplyDefaultSettings"));
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
    if (mealPlan.getDaysPassed() == 0) {
      dateUpdate.setEnabled(false);
      giveProposal.setEnabled(true);
    }
  }

  @Override
  public void updateData(DataStoreEventType event) {
    if (event == DataStoreEventType.DATE_UPDATED) {
      update();
    }
  }
}