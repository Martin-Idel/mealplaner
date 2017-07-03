package mealplaner.gui;
/**
 * Martin Idel, 
 * MainGUI: GUI parent: has the data base and Mealplaner and creates all other GUI. Saving and Loading from here.
 * Uses ProposalSummary and DatabaseEdit
 **/

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import mealplaner.MealplanerData;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.errorhandling.ErrorMessages;
import mealplaner.errorhandling.Logger;
import mealplaner.errorhandling.MealException;
import mealplaner.gui.commons.ButtonPanelBuilder;
import mealplaner.gui.commons.MenuBarBuilder;
import mealplaner.gui.databaseedit.DatabaseEdit;
import mealplaner.gui.dialogs.mealinput.MultipleMealInput;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.proposaloutput.ProposalTableFactory;
import mealplaner.gui.dialogs.proposaloutput.TablePrinter;
import mealplaner.io.MealplanerFileLoader;
import mealplaner.io.MealplanerFileSaver;
import mealplaner.model.Meal;

public class MainGUI implements ErrorKeys {

	private JFrame frame = new JFrame("Meal planer");

	private MealplanerData mealPlan;
	private DatabaseEdit dbaseEdit;
	private ProposalSummary proposalSummary;

	private Locale currentLocale;
	private ResourceBundle messages;

	public MainGUI(MealplanerData mealPlan) {
		this.mealPlan = mealPlan;

		loadResourceBundle();
		loadDatabase();

		JMenuBar menuBar = createMenuBar();

		JPanel buttonPanel = createButtonPanel();
		JPanel mealPanel = setupMealPanel(buttonPanel);
		JPanel databasePanel = setupDatabasePanel();
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add(messages.getString("menuPanel"), mealPanel);
		tabPane.add(messages.getString("dataPanel"), databasePanel);

		setupMainFrame(tabPane, menuBar);
	}

	private void loadResourceBundle() {
		try {
			currentLocale = Locale.getDefault();
			messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		} catch (MissingResourceException exc) {
			errorMessages(exc, ErrorMessages.formatMessage(MSG_MISSING_RBUNDLE));
		}
	}

	private void loadDatabase() {
		try {
			MealplanerData mealPlanLoaded = MealplanerFileLoader.load("save01.ser");
			this.mealPlan = mealPlanLoaded; // do not inline: This will only be
											// done when load was successful
			this.mealPlan.getMealListData().sortMealsAccordingToName();
		} catch (FileNotFoundException exc) {
			errorMessages(exc, ErrorMessages.formatMessage(MSG_FILE_NOT_FOUND));
		} catch (IOException exc) {
			errorMessages(exc, ErrorMessages.formatMessage(MSG_IOEX));
		} catch (MealException exc) {
			errorMessages(exc, ErrorMessages.formatMessage(MSG_CLASS_NOT_FOUND));
		}
	}

	public JMenuBar createMenuBar() {
		MenuBarBuilder builder = new MenuBarBuilder(frame, messages)
				.setupFileMenu()
				.createMealMenu(action -> {
					MultipleMealInput multipleMealInput = new MultipleMealInput(frame, messages);
					List<Meal> multipleMeals = multipleMealInput.showDialog();
					multipleMeals.forEach(meal -> mealPlan.getMealListData().addMealAtSortedPosition(meal));
					dbaseEdit.updateTable();
				})
				.viewProposalMenu(
						action -> new ProposalOutput(frame, mealPlan.getLastProposal(), currentLocale, messages))
				.createSeparatorForMenu();

		builder.createBackupMenu(action -> createBackup())
				.loadBackupMenu(action -> loadBackup())
				.createSeparatorForMenu();

		builder.printDatabaseMenu(action -> printDatabase())
				.printProposalMenu(action -> printProposal())
				.createSeparatorForMenu();

		builder.exitMenu(action -> showSaveExitDialog());

		builder.setupHelpMenu()
				.showDatabaseHelpMenu()
				.showHelpMenu();

		return builder.createMenuBar();
	}

	private JPanel createButtonPanel() {
		return new ButtonPanelBuilder(messages)
				.addButton("saveEndButton", "saveEndMnemonic", action -> {
					save();
					frame.dispose();
				})
				.addExitButton(action -> showSaveExitDialog())
				.build();
	}

	private JPanel setupDatabasePanel() {
		JPanel databasePanel = new JPanel();
		dbaseEdit = new DatabaseEdit(this.mealPlan, frame, databasePanel, messages);
		return databasePanel;
	}

	private JPanel setupMealPanel(JPanel buttonPanel) {
		JPanel mealPanel = new JPanel();
		mealPanel.setLayout(new BorderLayout());
		proposalSummary = new ProposalSummary(this.mealPlan, frame, currentLocale, messages);
		mealPanel.add(proposalSummary.buildProposalPanel(), BorderLayout.CENTER);
		mealPanel.add(buttonPanel, BorderLayout.SOUTH);
		return mealPanel;
	}

	private void setupMainFrame(JTabbedPane tabPane, JMenuBar menuBar) {
		frame.setTitle(messages.getString("planer"));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new SaveExitWindowListener());
		frame.setJMenuBar(menuBar);
		frame.add(tabPane, BorderLayout.CENTER);
		frame.setSize(550, 350);
		frame.setVisible(true);
	}

	public void save() {
		try {
			MealplanerFileSaver.save(mealPlan, "save01.ser");
			JOptionPane.showMessageDialog(frame, messages.getString("successSave"),
					messages.getString("successHeading"), JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException exc) {
			errorMessages(exc, ErrorMessages.formatMessage(MSG_IOEX));
		}
	}

	public void createBackup() {
		String bak = JOptionPane.showInputDialog(frame, messages.getString("createLoadBackup"), "*.ser");
		if (bak != null) {
			try {
				MealplanerFileSaver.save(mealPlan, bak);
				JOptionPane.showMessageDialog(frame, messages.getString("successSave"),
						messages.getString("successHeading"), JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException exc) {
				errorMessages(exc, ErrorMessages.formatMessage(MSG_IOEX));
			}
		}
	}

	public void loadBackup() {
		String bak = JOptionPane.showInputDialog(frame, messages.getString("createLoadBackup"), "*.ser");
		if (bak != null) {
			try {
				mealPlan = MealplanerFileLoader.load(bak);
				dbaseEdit.updateTable();
			} catch (FileNotFoundException exc) {
				errorMessages(exc, ErrorMessages.formatMessage(MSG_BKU_FILE_NOT_FOUND));
			} catch (IOException exc) {
				errorMessages(exc, ErrorMessages.formatMessage(MSG_IOEX));
			} catch (MealException exc) {
				errorMessages(exc, ErrorMessages.formatMessage(MSG_BKU_CLASS_NOT_FOUND));
			}
		}
	}

	private void errorMessages(Exception exc, String errorMessage) {
		JOptionPane.showMessageDialog(frame, ErrorMessages.formatMessage(MSG_BKU_CLASS_NOT_FOUND),
				ErrorMessages.formatMessage(ERR_HEADING), JOptionPane.ERROR_MESSAGE);
		Logger.logError(exc);
	}

	public void printDatabase() {
		dbaseEdit.printTable();
	}

	public void printProposal() {
		JTable proposalTable = new ProposalTableFactory(messages, currentLocale)
				.createTable(mealPlan.getLastProposal());
		TablePrinter.printTable(proposalTable, frame);
	}

	public class SaveExitWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			showSaveExitDialog();
		}
	}

	public void showSaveExitDialog() {
		int result = JOptionPane.showConfirmDialog(frame, messages.getString("saveYesNo"));
		if (result == JOptionPane.YES_OPTION) {
			save();
			frame.dispose();
		} else if (result == JOptionPane.NO_OPTION) {
			frame.dispose();
		}
	}
}