package mealplaner.gui;

import static mealplaner.errorhandling.ErrorMessages.formatMessage;
import static mealplaner.gui.commons.MessageDialog.errorMessages;
import static mealplaner.gui.commons.MessageDialog.showSaveExitDialog;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

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

	private Locale currentLocale;
	private ResourceBundle messages;
	private FileIOGui fileIOGui;

	public MainGUI(MealplanerData mealPlan) {
		this.mealPlan = mealPlan;

		loadResourceBundle();
		fileIOGui = new FileIOGui(messages, frame);
		this.mealPlan = fileIOGui.loadDatabase();

		JMenuBar menuBar = createMenuBar();

		JPanel buttonPanel = createButtonPanel();
		JPanel mealPanel = setupMealPanel(buttonPanel);
		JPanel databasePanel = setupDatabasePanel();
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(messages.getString("menuPanelName"), mealPanel);
		tabPane.add(messages.getString("dataPanelName"), databasePanel);

		setupMainFrame(tabPane, menuBar);
	}

	private void loadResourceBundle() {
		try {
			currentLocale = Locale.getDefault();
			messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		} catch (MissingResourceException exc) {
			errorMessages(frame, exc, formatMessage(MSG_MISSING_RBUNDLE));
		}
	}

	public JMenuBar createMenuBar() {
		MenuBarBuilder builder = new MenuBarBuilder(frame, messages)
				.setupFileMenu()
				.createMealMenu(action -> {
					MultipleMealInput multipleMealInput = new MultipleMealInput(frame, messages);
					multipleMealInput.showDialog()
							.forEach(meal -> mealPlan.addMeal(meal));
					dbaseEdit.updateTable();
				})
				.viewProposalMenu(
						action -> new ProposalOutput(frame, mealPlan.getLastProposal(),
								currentLocale, messages))
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
				action -> showSaveExitDialog(frame, messages.getString("saveYesNoQuestion"),
						() -> fileIOGui.saveDatabase(mealPlan)));

		builder.setupHelpMenu()
				.showDatabaseHelpMenu()
				.showHelpMenu();

		return builder.createMenuBar();
	}

	private JPanel createButtonPanel() {
		return new ButtonPanelBuilder(messages)
				.addButton("saveExitButton", "saveExitMnemonic", action -> {
					fileIOGui.saveDatabase(mealPlan);
					frame.dispose();
				})
				.addExitButton(
						action -> showSaveExitDialog(frame, messages.getString("saveYesNoQuestion"),
								() -> fileIOGui.saveDatabase(mealPlan)))
				.build();
	}

	private JPanel setupDatabasePanel() {
		JPanel databasePanel = new JPanel();
		dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel, messages);
		dbaseEdit.setupPane((meals) -> mealPlan.setMeals(meals));
		return databasePanel;
	}

	private JPanel setupMealPanel(JPanel buttonPanel) {
		JPanel mealPanel = new JPanel();
		mealPanel.setLayout(new BorderLayout());
		proposalSummary = new ProposalSummary(this.mealPlan, frame, currentLocale, messages);
		mealPanel.add(proposalSummary.buildProposalPanel(
				action -> updatePastMeals(),
				action -> changeDefaultSettings(),
				action -> makeProposal()),
				BorderLayout.CENTER);
		mealPanel.add(buttonPanel, BorderLayout.SOUTH);
		return mealPanel;
	}

	private void setupMainFrame(JTabbedPane tabPane, JMenuBar menuBar) {
		frame.setTitle(messages.getString("mainFrameTitle"));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new SaveExitWindowListener());
		frame.setJMenuBar(menuBar);
		frame.add(tabPane, BorderLayout.CENTER);
		frame.setSize(550, 350);
		frame.setVisible(true);
	}

	public void printProposal() {
		JTable proposalTable = new ProposalTableFactory(messages, currentLocale)
				.createTable(mealPlan.getLastProposal());
		TablePrinter.printTable(proposalTable, frame);
	}

	public void makeProposal() {
		ProposalOutline outline = proposalSummary.getProposalOutline();
		if (outline.isIncludedToday()) {
			Settings[] settings = setDefaultSettings(outline);
			makeProposal(settings, outline);
		} else {
			Optional<Settings[]> settingsInput = new ProposalSettingsInput(frame,
					mealPlan.getDefaultSettings(), outline, messages, currentLocale)
							.showDialog();
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
		new ProposalOutput(frame, mealPlan.getLastProposal(), currentLocale, messages);
	}

	private Proposal propose(Settings[] set, boolean today, boolean random) {
		return new ProposalBuilder()
				.firstProposal(today)
				.randomise(random)
				.propose(mealPlan.getMeals(), set);
	}

	public void changeDefaultSettings() {
		Optional<Settings[]> defaultSettings = new DefaultSettingsInput(frame,
				mealPlan.getDefaultSettings(), messages)
						.showDialog();
		defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
	}

	public void updatePastMeals() {
		List<Meal> lastCookedMealList = new UpdatePastMeals(frame, mealPlan, currentLocale,
				messages).showDialog();
		mealPlan.update(lastCookedMealList);
		proposalSummary.update();
	}

	public class SaveExitWindowListener extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			showSaveExitDialog(frame, messages.getString("saveYesNoQuestion"),
					() -> fileIOGui.saveDatabase(mealPlan));
		}
	}
}