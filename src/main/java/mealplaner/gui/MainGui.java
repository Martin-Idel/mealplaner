package mealplaner.gui;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;
import static mealplaner.gui.HelpMenu.createHelpMenu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;

import mealplaner.MealplanerData;
import mealplaner.ProposalBuilder;
import mealplaner.commons.gui.MenuBarBuilder;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.dialogs.proposaloutput.ProposalTable;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.gui.tabbedpanes.databaseedit.DatabaseEdit;
import mealplaner.gui.tabbedpanes.proposal.ProposalSummary;
import mealplaner.io.FileIoGui;
import mealplaner.io.IngredientIo;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.provider.IngredientProvider;

public class MainGui {
  private final JFrame frame;
  private final DialogFactory dialogs;
  private final MainContainer container;

  private final IngredientProvider ingredients;
  private MealplanerData mealPlan;
  private DatabaseEdit dbaseEdit;
  private ProposalSummary proposalSummary;

  private final FileIoGui fileIoGui;

  public MainGui(JFrame mainFrame, MealplanerData mealPlan, DialogFactory dialogFactory,
      IngredientProvider ingredientProvider) {
    this.frame = mainFrame;
    this.mealPlan = mealPlan;
    this.dialogs = dialogFactory;
    this.ingredients = ingredientProvider;

    fileIoGui = new FileIoGui(frame);
    this.mealPlan = fileIoGui.loadDatabase();
    this.container = new MainContainer(frame);

    container.addMenu(createFileMenu());
    container.addMenu(createHelpMenu(frame));

    ButtonPanel buttonPanel = createButtonPanel();
    JPanel mealPanel = setupMealPanel(buttonPanel.getComponent());
    JPanel databasePanel = setupDatabasePanel();
    container.addTabbedPane(BUNDLES.message("menuPanelName"), mealPanel);
    container.addTabbedPane(BUNDLES.message("dataPanelName"), databasePanel);

    container.setupMainFrame(new SaveExitWindowListener());
  }

  private JMenu createFileMenu() {
    MenuBarBuilder builder = new MenuBarBuilder(frame)
        .setupFileMenu()
        .createIngredientsMenu(action -> {
          dialogs.createIngredientsInput()
              .showDialog()
              .forEach(ingredients::add);
          IngredientIo.saveXml(ingredients);
        })
        .createMealMenu(action -> {
          dialogs.createMultipleMealInputDialog()
              .showDialog(ingredients)
              .forEach(meal -> mealPlan.addMeal(meal));
          // TODO: this should be unnecessary
          dbaseEdit.updateTable();
        })
        .viewProposalMenu(action -> dialogs.createProposalOutputDialog()
            .showDialog(mealPlan.getMeals(), mealPlan.getLastProposal()))
        .createSeparatorForMenu();

    // TODO: Database backup loading can't work that way. We need to keep the
    // mealPlan or reload the complete GUI
    builder.createBackupMenu(action -> fileIoGui.createBackup(mealPlan))
        .loadBackupMenu(action -> {
          fileIoGui.loadBackup()
              .ifPresent(loadedMealPlaner -> this.mealPlan = loadedMealPlaner);
          dbaseEdit.updateTable();
        })
        .createSeparatorForMenu();

    builder.printDatabaseMenu(action -> dbaseEdit.printTable())
        .printProposalMenu(action -> printProposal())
        .createSeparatorForMenu();

    builder.exitMenu(
        action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
            () -> fileIoGui.saveDatabase(mealPlan)));

    return builder.createMenuBar();
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

  private JPanel setupDatabasePanel() {
    JPanel databasePanel = new JPanel();
    dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel);
    dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals), ingredients);
    return databasePanel;
  }

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

  public void printProposal() {
    ProposalTable proposalTable = dialogs.createProposalTableFactory();
    proposalTable.setupProposalTable(mealPlan.getLastProposal());
    proposalTable.getTable().printTable(frame);
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

  public void updatePastMeals() {
    Optional<List<Meal>> lastCookedMealList = dialogs.createUpdatePastMealDialog()
        .showDialog(mealPlan);
    lastCookedMealList.ifPresent(list -> mealPlan.update(list, now()));
    proposalSummary.update();
  }

  JFrame getFrame() {
    return frame;
  }

  void saveDataBase() {
    fileIoGui.saveDatabase(mealPlan);
  }

  public class SaveExitWindowListener extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      showSaveExitDialog(getFrame(), BUNDLES.message("saveYesNoQuestion"),
          () -> saveDataBase());
    }
  }
}