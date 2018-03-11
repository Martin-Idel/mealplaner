package mealplaner.gui.tabbedpanes.proposal;

import static java.time.LocalDate.now;
import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.MessageDialog.showSaveExitDialog;
import static mealplaner.commons.gui.buttonpanel.ButtonPanelBuilder.builder;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import mealplaner.MealplanerData;
import mealplaner.ProposalBuilder;
import mealplaner.commons.gui.JMenuBuilder;
import mealplaner.commons.gui.buttonpanel.ButtonPanel;
import mealplaner.gui.MainContainer;
import mealplaner.gui.dialogs.proposaloutput.ProposalTable;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.provider.IngredientProvider;
import mealplaner.xml.IngredientsWriter;

public class ProposalSummaryPanel {
  private final MealplanerData mealPlan;
  private final DialogFactory dialogs;
  private final JFrame frame;
  private final IngredientProvider ingredients;

  private ProposalSummary proposalSummary;
  private JPanel mealPanel;

  public ProposalSummaryPanel(
      MealplanerData mealPlan,
      DialogFactory dialogs,
      JFrame frame,
      IngredientProvider ingredients) {
    this.mealPlan = mealPlan;
    this.dialogs = dialogs;
    this.frame = frame;
    this.ingredients = ingredients;
  }

  public void setupPanel(ActionListener saveExitListener, Runnable saveAction) {
    setupMealPanel(saveExitListener, saveAction);
  }

  public void addElements(MainContainer container) {
    container.addTabbedPane(BUNDLES.message("menuPanelName"), mealPanel);
    addToFileMenu(container);
  }

  private void setupMealPanel(ActionListener saveExitListener, Runnable saveAction) {
    ButtonPanel buttonPanel = createButtonPanel(saveExitListener, saveAction);
    mealPanel = new JPanel();
    mealPanel.setLayout(new BorderLayout());
    proposalSummary = new ProposalSummary(this.mealPlan);
    mealPanel.add(proposalSummary.buildProposalPanel(
        action -> updatePastMeals(),
        action -> changeDefaultSettings(),
        action -> makeProposal()).getComponent(),
        BorderLayout.CENTER);
    mealPanel.add(buttonPanel.getComponent(), BorderLayout.SOUTH);
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

  private ButtonPanel createButtonPanel(ActionListener saveExitListener,
      Runnable saveAction) {
    return builder("ProposalSummary")
        .addButton(BUNDLES.message("saveExitButton"),
            BUNDLES.message("saveExitMnemonic"),
            saveExitListener)
        .addExitButton(
            action -> showSaveExitDialog(frame, BUNDLES.message("saveYesNoQuestion"),
                saveAction))
        .build();
  }

  private void addToFileMenu(MainContainer container) {
    container.addToFileMenu(createIngredientsMenu(action -> {
      dialogs.createIngredientsInput()
          .showDialog()
          .forEach(ingredients::add);
      IngredientsWriter.saveXml(ingredients.getIngredients(), ingredients.getSavePath());
    }));
    container.addToFileMenu(createMealMenu(action -> {
      dialogs.createMultipleMealInputDialog()
          .showDialog(ingredients)
          .forEach(meal -> mealPlan.addMeal(meal));
    }));
    container.addToFileMenu(viewProposalMenu(action -> dialogs
        .createProposalOutputDialog()
        .showDialog(mealPlan.getMeals(), mealPlan.getLastProposal())));
    container.addSeparatorToFileMenu();

    container.addToFileMenu(printProposalMenu(action -> printProposal()));
    container.addSeparatorToFileMenu();
  }

  private void printProposal() {
    ProposalTable proposalTable = dialogs.createProposalTableFactory();
    proposalTable.setupProposalTable(mealPlan.getLastProposal());
    proposalTable.getTable().printTable(frame);
  }

  private JMenuItem printProposalMenu(ActionListener listener) {
    return JMenuBuilder.builder("PrintProposal")
        .addLabelText(BUNDLES.message("menuDataPrintProposal"))
        .addMnemonic(BUNDLES.message("menuDataPrintProposalMnemonic"))
        .addActionListener(listener)
        .build();
  }

  private JMenuItem createIngredientsMenu(ActionListener listener) {
    return JMenuBuilder.builder("InsertIngredients")
        .addLabelText(BUNDLES.message("ingredientInsertMenu"))
        .addMnemonic(BUNDLES.message("ingredientInsertMenuMnemonic"))
        .addActionListener(listener)
        .build();
  }

  private JMenuItem createMealMenu(ActionListener listener) {
    return JMenuBuilder.builder("CreateMeal")
        .addLabelText(BUNDLES.message("menuDataCreateMenu"))
        .addMnemonic(BUNDLES.message("menuDataCreateMenuMnemonic"))
        .addActionListener(listener)
        .build();
  }

  private JMenuItem viewProposalMenu(ActionListener listener) {
    return JMenuBuilder.builder("LastProposal")
        .addLabelText(BUNDLES.message("menuDataLastProposal"))
        .addMnemonic(BUNDLES.message("menuDataLastProposalMnemonic"))
        .addActionListener(listener)
        .build();
  }
}
