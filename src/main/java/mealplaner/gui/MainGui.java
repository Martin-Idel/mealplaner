package mealplaner.gui;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

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

    JMenuBar menuBar = createMenuBar();

    ButtonPanel buttonPanel = createButtonPanel();
    JPanel mealPanel = setupMealPanel(buttonPanel.getComponent());
    JPanel databasePanel = setupDatabasePanel();
    JTabbedPane tabPane = new JTabbedPane();
    tabPane.add(BUNDLES.message("menuPanelName"), mealPanel);
    tabPane.add(BUNDLES.message("dataPanelName"), databasePanel);

    setupMainFrame(tabPane, menuBar);
  }

  private JMenuBar createMenuBar() {
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
          dbaseEdit.updateTable();
        })
        .viewProposalMenu(action -> dialogs.createProposalOutputDialog()
            .showDialog(mealPlan.getMeals(), mealPlan.getLastProposal()))
        .createSeparatorForMenu();

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

    builder.setupHelpMenu()
        .showDatabaseHelpMenu()
        .showHelpMenu();

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

  private void setupMainFrame(JTabbedPane tabPane, JMenuBar menuBar) {
    frame.setTitle(BUNDLES.message("mainFrameTitle"));
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.addWindowListener(new SaveExitWindowListener());
    frame.setJMenuBar(menuBar);
    frame.add(tabPane, BorderLayout.CENTER);
    frame.setSize(550, 350);
    frame.setVisible(true);
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