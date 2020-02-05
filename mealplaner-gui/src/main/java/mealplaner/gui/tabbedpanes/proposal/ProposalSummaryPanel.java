// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.proposal;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.tabbedpanes.proposal.ProposalSummaryMenuItems.helpMenu;
import static mealplaner.gui.tabbedpanes.proposal.ProposalSummaryMenuItems.printProposalMenu;
import static mealplaner.gui.tabbedpanes.proposal.ProposalSummaryMenuItems.viewProposalMenu;

import java.awt.BorderLayout;
import java.time.DayOfWeek;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.api.ProposalBuilderFactory;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.MainContainer;
import mealplaner.gui.dialogs.proposaloutput.ProposalTable;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.ioapi.FileIoInterface;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposalOutline;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import mealplaner.plugins.PluginStore;
import mealplaner.plugins.api.ProposalBuilderStep;

public class ProposalSummaryPanel {
  private final MealplanerData mealPlan;
  private final DialogFactory dialogs;
  private final JFrame frame;
  private final FileIoInterface fileIo;
  private final ProposalBuilderFactory proposalFactory;

  private ProposalSummary proposalSummary;
  private JPanel mealPanel;

  public ProposalSummaryPanel(
      MealplanerData mealPlan,
      DialogFactory dialogs,
      JFrame frame,
      FileIoInterface fileIo, ProposalBuilderFactory proposalFactory) {
    this.mealPlan = mealPlan;
    this.dialogs = dialogs;
    this.frame = frame;
    this.fileIo = fileIo;
    this.proposalFactory = proposalFactory;
  }

  public void setupPanel(PluginStore pluginStore) {
    mealPanel = new JPanel();
    mealPanel.setLayout(new BorderLayout());
    proposalSummary = new ProposalSummary(this.mealPlan);
    mealPanel.add(proposalSummary.buildProposalPanel(
        action -> updatePastMeals(pluginStore),
        action -> changeDefaultSettings(pluginStore),
        action -> makeProposal(pluginStore)).getComponent(),
        BorderLayout.CENTER);
    ButtonPanel buttonPanel = createButtonPanel();
    mealPanel.add(buttonPanel.getComponent(), BorderLayout.SOUTH);
  }

  public void addElements(MainContainer container, PluginStore pluginStore) {
    container.addTabbedPane(BUNDLES.message("menuPanelName"), mealPanel);
    addToFileMenu(container, pluginStore);
    container.addToHelpMenu(helpMenu(frame));
  }

  private void makeProposal(PluginStore pluginStore) {
    ProposalOutline outline = proposalSummary.getProposalOutline();
    if (outline.takeDefaultSettings()) {
      Settings[] settings = setDefaultSettings(outline);
      createProposal(settings, outline, pluginStore);
    } else {
      Optional<Settings[]> settingsInput = dialogs
          .createProposalSettingsDialog()
          .showDialog(outline, mealPlan, pluginStore);
      settingsInput.ifPresent(settings -> createProposal(settings, outline, pluginStore));
    }
  }

  private Settings[] setDefaultSettings(ProposalOutline outline) {
    Settings[] settings = new Settings[outline.getNumberOfDays()];
    Map<DayOfWeek, Settings> defaultSettings = mealPlan.getDefaultSettings()
        .getDefaultSettingsMap();
    DayOfWeek dayOfWeek = outline.isIncludedToday()
        ? mealPlan.getTime().getDayOfWeek()
        : mealPlan.getTime().getDayOfWeek().plus(1);
    for (int i = 0; i < settings.length; i++) {
      settings[i] = defaultSettings.get(dayOfWeek);
      dayOfWeek = dayOfWeek.plus(1);
    }
    return settings;
  }

  private void createProposal(Settings[] settings, ProposalOutline outline, PluginStore pluginStore) {
    Proposal proposal = propose(settings, outline.isIncludedToday(),
        outline.isShallBeRandomised(), pluginStore.getRegisteredProposalBuilderSteps());
    mealPlan.setLastProposal(proposal);
    Proposal updatedProposal = dialogs.createProposalOutputDialog()
        .showDialog(mealPlan, pluginStore);
    mealPlan.setLastProposal(updatedProposal);
    dialogs.createShoppingListDialog().showDialog(updatedProposal, mealPlan);
  }

  private void updatePastMeals(PluginStore pluginStore) {
    Optional<List<ProposedMenu>> lastCookedMealList = dialogs.createUpdatePastMealDialog()
        .showDialog(mealPlan, pluginStore);
    lastCookedMealList.ifPresent(list -> mealPlan.update(list, now()));
    proposalSummary.update();
  }

  private Proposal propose(
      Settings[] set, boolean today, boolean random, Collection<ProposalBuilderStep> proposalBuilderSteps) {
    return proposalFactory.createProposalBuilder(mealPlan.getMeals(), proposalBuilderSteps)
        .firstProposal(today)
        .randomise(random)
        .propose(set);
  }

  private void changeDefaultSettings(PluginStore pluginStore) {
    Optional<DefaultSettings> defaultSettings = dialogs
        .createDefaultSettingsDialog()
        .showDialog(mealPlan, pluginStore);
    defaultSettings.ifPresent(mealPlan::setDefaultSettings);
  }

  private ButtonPanel createButtonPanel() {
    return builder("ProposalSummary")
        .addButton(BUNDLES.message("saveExitButton"),
            BUNDLES.message("saveExitMnemonic"),
            action -> {
              fileIo.saveDatabase(mealPlan);
              frame.dispose();
            })
        .addExitButton(
            action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
                () -> fileIo.saveDatabase(mealPlan)))
        .build();
  }

  private void addToFileMenu(MainContainer container, PluginStore pluginStore) {
    container.addToFileMenu(viewProposalMenu(action -> dialogs
        .createProposalOutputDialog()
        .showDialog(mealPlan, pluginStore)));
    container.addSeparatorToFileMenu();

    container.addToFileMenu(printProposalMenu(action -> printProposal()));
    container.addSeparatorToFileMenu();
  }

  private void printProposal() {
    ProposalTable proposalTable = dialogs.createProposalTableFactory();
    proposalTable.setupProposalTable(mealPlan, mealPlan.getLastProposal());
    proposalTable.getTable().printTable(frame);
  }
}
