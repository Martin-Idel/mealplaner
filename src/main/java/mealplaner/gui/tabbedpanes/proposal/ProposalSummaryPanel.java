package mealplaner.gui.tabbedpanes.proposal;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.tabbedpanes.proposal.ProposalSummaryMenuItems.printProposalMenu;
import static mealplaner.gui.tabbedpanes.proposal.ProposalSummaryMenuItems.viewProposalMenu;

import java.awt.BorderLayout;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mealplaner.ProposalBuilder;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.MainContainer;
import mealplaner.gui.dialogs.proposaloutput.ProposalTable;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIoGui;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.proposal.ProposalOutline;
import mealplaner.model.proposal.ProposedMenu;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;

public class ProposalSummaryPanel {
  private final MealplanerData mealPlan;
  private final DialogFactory dialogs;
  private final JFrame frame;
  private final FileIoGui fileIoGui;

  private ProposalSummary proposalSummary;
  private JPanel mealPanel;

  public ProposalSummaryPanel(
      MealplanerData mealPlan,
      DialogFactory dialogs,
      JFrame frame,
      FileIoGui fileIoGui) {
    this.mealPlan = mealPlan;
    this.dialogs = dialogs;
    this.frame = frame;
    this.fileIoGui = fileIoGui;
  }

  public void setupPanel() {
    mealPanel = new JPanel();
    mealPanel.setLayout(new BorderLayout());
    proposalSummary = new ProposalSummary(this.mealPlan);
    mealPanel.add(proposalSummary.buildProposalPanel(
        action -> updatePastMeals(),
        action -> changeDefaultSettings(),
        action -> makeProposal()).getComponent(),
        BorderLayout.CENTER);
    ButtonPanel buttonPanel = createButtonPanel();
    mealPanel.add(buttonPanel.getComponent(), BorderLayout.SOUTH);
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("menuPanelName"), mealPanel);
    addToFileMenu(container);
  }

  public void makeProposal() {
    ProposalOutline outline = proposalSummary.getProposalOutline();
    if (outline.takeDefaultSettings()) {
      Settings[] settings = setDefaultSettings(outline);
      createProposal(settings, outline);
    } else {
      Optional<Settings[]> settingsInput = dialogs
          .createProposalSettingsDialog()
          .showDialog(outline, mealPlan);
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
        .showDialog(mealPlan);
    mealPlan.setLastProposal(updatedProposal);
    dialogs.createShoppingListDialog().showDialog(updatedProposal, mealPlan);
  }

  public void updatePastMeals() {
    Optional<List<ProposedMenu>> lastCookedMealList = dialogs.createUpdatePastMealDialog()
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
        .showDialog(mealPlan);
    defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
  }

  private ButtonPanel createButtonPanel() {
    return builder("ProposalSummary")
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

  private void addToFileMenu(MainContainer container) {
    container.addToFileMenu(viewProposalMenu(action -> dialogs
        .createProposalOutputDialog()
        .showDialog(mealPlan)));
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
