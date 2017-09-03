package mealplaner.gui;

import static mealplaner.gui.commons.MessageDialog.showSaveExitDialog;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
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
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.MenuBarBuilder;
import mealplaner.gui.databaseedit.DatabaseEdit;
import mealplaner.gui.dialogs.mealinput.MultipleMealInput;
import mealplaner.gui.dialogs.pastupdate.UpdatePastMeals;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.proposaloutput.ProposalTableFactory;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.gui.dialogs.settingsinput.DefaultSettingsInput;
import mealplaner.gui.dialogs.settingsinput.ProposalSettingsInput;
import mealplaner.io.FileIOGui;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class MainGUI implements ErrorKeys {
	private JFrame frame = new JFrame("Meal planer");

	private MealplanerData mealPlan;
	private DatabaseEdit dbaseEdit;
	private ProposalSummary proposalSummary;

	private BundleStore bundles;
	private FileIOGui fileIOGui;

	public MainGUI(MealplanerData mealPlan, BundleStore bundles) {
		this.mealPlan = mealPlan;
		this.bundles = bundles;

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
				.createMealMenu(action -> {
					MultipleMealInput multipleMealInput = new MultipleMealInput(frame, bundles);
					multipleMealInput.showDialog()
							.forEach(meal -> mealPlan.addMeal(meal));
					dbaseEdit.updateTable();
				})
				.viewProposalMenu(
						action -> new ProposalOutput(frame, mealPlan.getLastProposal(), bundles))
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
		dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals));
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
		JTable proposalTable = new ProposalTableFactory(bundles)
				.createTable(mealPlan.getLastProposal());
		TablePrinter.printTable(proposalTable, frame, bundles);
	}

	public void makeProposal() {
		ProposalOutline outline = proposalSummary.getProposalOutline();
		if (outline.isIncludedToday()) {
			Settings[] settings = setDefaultSettings(outline);
			makeProposal(settings, outline);
		} else {
			Optional<Settings[]> settingsInput = new ProposalSettingsInput(frame,
					mealPlan.getDefaultSettings(), outline, bundles).showDialog();
			settingsInput.ifPresent(settings -> makeProposal(settings, outline));
		}
	}

	private Settings[] setDefaultSettings(ProposalOutline outline) {
		Settings[] settings = new Settings[outline.getNumberOfDays()];
		int dayOfWeek = mealPlan.getToday();
		Settings[] defaultSettings = mealPlan.getDefaultSettings();
		if (!outline.isIncludedToday()) {
			dayOfWeek++;
		}
		for (int i = 0; i < settings.length; i++) {
			settings[i] = defaultSettings[(dayOfWeek - 2) % 7];
			dayOfWeek++;
		}
		return settings;
	}

	private void makeProposal(Settings[] settings, ProposalOutline outline) {
		Proposal proposal = propose(settings, outline.isIncludedToday(),
				outline.isShallBeRandomised());
		mealPlan.setLastProposal(proposal);
		new ProposalOutput(frame, mealPlan.getLastProposal(), bundles);
	}

	private Proposal propose(Settings[] set, boolean today, boolean random) {
		return new ProposalBuilder()
				.firstProposal(today)
				.randomise(random)
				.propose(mealPlan.getMeals(), set);
	}

	public void changeDefaultSettings() {
		Optional<Settings[]> defaultSettings = new DefaultSettingsInput(frame,
				mealPlan.getDefaultSettings(), bundles)
						.showDialog();
		defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
	}

	public void updatePastMeals() {
		List<Meal> lastCookedMealList = new UpdatePastMeals(frame, mealPlan, bundles).showDialog();
		mealPlan.update(lastCookedMealList);
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