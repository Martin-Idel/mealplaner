package mealplaner.gui;

import static java.time.LocalDate.now;
import static mealplaner.gui.commons.MessageDialog.showSaveExitDialog;

import java.awt.BorderLayout;
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
import javax.swing.JTable;
import javax.swing.WindowConstants;

import mealplaner.BundleStore;
import mealplaner.MealplanerData;
import mealplaner.ProposalBuilder;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.MenuBarBuilder;
import mealplaner.gui.databaseedit.DatabaseEdit;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.gui.factories.DialogFactory;
import mealplaner.io.FileIOGui;
import mealplaner.io.IngredientIO;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;
import mealplaner.recipes.provider.IngredientProvider;

public class MainGUI {
	private JFrame frame;
	private DialogFactory dialogs;

	private IngredientProvider ingredients;
	private MealplanerData mealPlan;
	private DatabaseEdit dbaseEdit;
	private ProposalSummary proposalSummary;

	private BundleStore bundles;
	private FileIOGui fileIOGui;

	public MainGUI(JFrame mainFrame, MealplanerData mealPlan, DialogFactory dialogFactory,
			BundleStore bundles, IngredientProvider ingredientProvider) {
		this.frame = mainFrame;
		this.mealPlan = mealPlan;
		this.dialogs = dialogFactory;
		this.bundles = bundles;
		this.ingredients = ingredientProvider;

		fileIOGui = new FileIOGui(bundles, frame);
		this.mealPlan = fileIOGui.loadDatabase();

		JMenuBar menuBar = createMenuBar();

		JPanel buttonPanel = createButtonPanel();
		JPanel mealPanel = setupMealPanel(buttonPanel);
		JPanel databasePanel = setupDatabasePanel();
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(bundles.message("menuPanelName"), mealPanel);
		tabPane.add(bundles.message("dataPanelName"), databasePanel);

		setupMainFrame(tabPane, menuBar);
	}

	public JMenuBar createMenuBar() {
		MenuBarBuilder builder = new MenuBarBuilder(frame, bundles)
				.setupFileMenu()
				.createIngredientsMenu(action -> {
					dialogs.createIngredientsInput()
							.showDialog()
							.forEach(ingredients::add);
					IngredientIO.saveXml(ingredients);
				})
				.createMealMenu(action -> {
					dialogs.createMultipleMealInputDialog(ingredients)
							.showDialog()
							.forEach(meal -> mealPlan.addMeal(meal));
					dbaseEdit.updateTable();
				})
				.viewProposalMenu(action -> dialogs.createProposalOutputDialog()
						.showDialog(mealPlan.getLastProposal()))
				.createSeparatorForMenu();

		builder.createBackupMenu(action -> fileIOGui.createBackup(mealPlan))
				.loadBackupMenu(action -> {
					fileIOGui.loadBackup()
							.ifPresent(loadedMealPlaner -> this.mealPlan = loadedMealPlaner);
					dbaseEdit.updateTable();
				})
				.createSeparatorForMenu();

		builder.printDatabaseMenu(action -> dbaseEdit.printTable())
				.printProposalMenu(action -> printProposal())
				.createSeparatorForMenu();

		builder.exitMenu(
				action -> showSaveExitDialog(frame, bundles.message("saveYesNoQuestion"),
						() -> fileIOGui.saveDatabase(mealPlan)));

		builder.setupHelpMenu()
				.showDatabaseHelpMenu()
				.showHelpMenu();

		return builder.createMenuBar();
	}

	private JPanel createButtonPanel() {
		return new ButtonPanelBuilder(bundles)
				.addButton(bundles.message("saveExitButton"),
						bundles.message("saveExitMnemonic"),
						action -> {
							fileIOGui.saveDatabase(mealPlan);
							frame.dispose();
						})
				.addExitButton(
						action -> showSaveExitDialog(frame, bundles.message("saveYesNoQuestion"),
								() -> fileIOGui.saveDatabase(mealPlan)))
				.build();
	}

	private JPanel setupDatabasePanel() {
		JPanel databasePanel = new JPanel();
		dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel, bundles);
		dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals), ingredients);
		return databasePanel;
	}

	private JPanel setupMealPanel(JPanel buttonPanel) {
		JPanel mealPanel = new JPanel();
		mealPanel.setLayout(new BorderLayout());
		proposalSummary = new ProposalSummary(this.mealPlan, frame, bundles);
		mealPanel.add(proposalSummary.buildProposalPanel(
				action -> updatePastMeals(),
				action -> changeDefaultSettings(),
				action -> makeProposal()),
				BorderLayout.CENTER);
		mealPanel.add(buttonPanel, BorderLayout.SOUTH);
		return mealPanel;
	}

	private void setupMainFrame(JTabbedPane tabPane, JMenuBar menuBar) {
		frame.setTitle(bundles.message("mainFrameTitle"));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new SaveExitWindowListener());
		frame.setJMenuBar(menuBar);
		frame.add(tabPane, BorderLayout.CENTER);
		frame.setSize(550, 350);
		frame.setVisible(true);
	}

	public void printProposal() {
		JTable proposalTable = dialogs.createProposalTableFactory()
				.createTable(mealPlan.getLastProposal());
		TablePrinter.printTable(proposalTable, frame, bundles);
	}

	public void makeProposal() {
		ProposalOutline outline = proposalSummary.getProposalOutline();
		if (outline.isIncludedToday()) {
			Settings[] settings = setDefaultSettings(outline);
			makeProposal(settings, outline);
		} else {
			Optional<Settings[]> settingsInput = dialogs
					.createProposalSettingsDialog()
					.showDialog(mealPlan.getDefaultSettings(), outline);
			settingsInput.ifPresent(settings -> makeProposal(settings, outline));
		}
	}

	private Settings[] setDefaultSettings(ProposalOutline outline) {
		Settings[] settings = new Settings[outline.getNumberOfDays()];
		Map<DayOfWeek, Settings> defaultSettings = mealPlan.getDefaultSettings()
				.getDefaultSettings();
		DayOfWeek dayOfWeek = !outline.isIncludedToday()
				? mealPlan.getTime().getDayOfWeek().plus(1)
				: mealPlan.getTime().getDayOfWeek();
		for (int i = 0; i < settings.length; i++) {
			settings[i] = defaultSettings.get(dayOfWeek);
			dayOfWeek = dayOfWeek.plus(1);
		}
		return settings;
	}

	private void makeProposal(Settings[] settings, ProposalOutline outline) {
		Proposal proposal = propose(settings, outline.isIncludedToday(),
				outline.isShallBeRandomised());
		mealPlan.setLastProposal(proposal);
		dialogs.createProposalOutputDialog().showDialog(proposal);
	}

	private Proposal propose(Settings[] set, boolean today, boolean random) {
		return new ProposalBuilder()
				.firstProposal(today)
				.randomise(random)
				.propose(mealPlan.getMeals(), set);
	}

	public void changeDefaultSettings() {
		Optional<DefaultSettings> defaultSettings = dialogs
				.createDefaultSettingsDialog()
				.showDialog(mealPlan.getDefaultSettings());
		defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
	}

	public void updatePastMeals() {
		List<Meal> lastCookedMealList = dialogs.createUpdatePastMealDialog().showDialog(mealPlan);
		mealPlan.update(lastCookedMealList, now());
		proposalSummary.update();
	}

	public class SaveExitWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			showSaveExitDialog(frame, bundles.message("saveYesNoQuestion"),
					() -> fileIOGui.saveDatabase(mealPlan));
		}
	}
}