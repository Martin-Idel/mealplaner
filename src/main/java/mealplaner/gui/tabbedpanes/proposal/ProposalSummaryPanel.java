package mealplaner.gui.tabbedpanes.proposal;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JPanel;

import mealplaner.MealplanerData;
import mealplaner.ProposalBuilder;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class ProposalSummaryPanel {

  private ProposalSummary proposalSummary;
  private MealplanerData mealPlan;
  private DialogFactory dialogs;

  private JPanel setupMealPanel(Component buttonPanel) {
    JPanel mealPanel = new JPanel();
    mealPanel.setLayout(new BorderLayout());
    proposalSummary = new ProposalSummary(this.mealPlan);
    mealPanel.add(proposalSummary.buildProposalPanel(
        action -> updatePastMeals(),
        action -> changeDefaultSettings(),
        action -> makeProposal()).getComponent(),
        BorderLayout.CENTER);
    mealPanel.add(buttonPanel, BorderLayout.SOUTH);
    return mealPanel;
  }

  public void makeProposal() {
    ProposalOutline outline = proposalSummary.getProposalOutline();
    if (outline.takeDefaultSettings()) {
      Settings[] settings = setDefaultSettings(outline);
      createProposal(settings, outline);
    } else {
      Optional<Settings[]> settingsInput = dialogs
          .createProposalSettingsDialog()
          .showDialog(mealPlan.getDefaultSettings(), outline);
      settingsInput.ifPresent(settings -> createProposal(settings, outline));
    }
  }

  private Settings[] setDefaultSettings(ProposalOutline outline) {
    Settings[] settings = new Settings[outline.getNumberOfDays()];
    Map<DayOfWeek, Settings> defaultSettings = mealPlan.getDefaultSettings()
        .getDefaultSettings();
    DayOfWeek dayOfWeek = outline.isIncludedToday()
        ? mealPlan.getTime().getDayOfWeek()
        : mealPlan.getTime().getDayOfWeek().plus(1);
    for (int i = 0; i < settings.length; i++) {
      settings[i] = defaultSettings.get(dayOfWeek);
      dayOfWeek = dayOfWeek.plus(1);
    }
    return settings;
  }

  private void createProposal(Settings[] settings, ProposalOutline outline) {
    Proposal proposal = propose(settings, outline.isIncludedToday(),
        outline.isShallBeRandomised());
    mealPlan.setLastProposal(proposal);
    Proposal updatedProposal = dialogs.createProposalOutputDialog()
        .showDialog(mealPlan.getMeals(), proposal);
    mealPlan.setLastProposal(updatedProposal);
    dialogs.createShoppingListDialog().showDialog(updatedProposal, ingredients);
  }

  public void updatePastMeals() {
    Optional<List<Meal>> lastCookedMealList = dialogs.createUpdatePastMealDialog()
        .showDialog(mealPlan);
    lastCookedMealList.ifPresent(list -> mealPlan.update(list, now()));
    proposalSummary.update();
  }

  private Proposal propose(Settings[] set, boolean today, boolean random) {
    return new ProposalBuilder()
        .firstProposal(today)
        .randomise(random)
        .propose(set, mealPlan.getMeals());
  }

  public void changeDefaultSettings() {
    Optional<DefaultSettings> defaultSettings = dialogs
        .createDefaultSettingsDialog()
        .showDialog(mealPlan.getDefaultSettings());
    defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
  }

  private ButtonPanel createButtonPanel() {
    return builder()
        .addButton(BUNDLES.message("saveExitButton"),
            BUNDLES.message("saveExitMnemonic"),
            action -> {
              fileIoGui.saveDatabase(mealPlan);
              frame.dispose();
            })
        .addExitButton(
            action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
                () -> fileIoGui.saveDatabase(mealPlan)))
        .build();
  }

}
