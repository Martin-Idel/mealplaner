package mealplaner.gui;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.errorhandling.ErrorKeys;
import mealplaner.gui.commons.CheckboxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.model.settings.ProposalOutline;

public class ProposalSummary implements DataStoreListener, ErrorKeys {
	private DataStore mealPlan;
	private JPanel dataPanel;
	private JLabel dateShow;
	private JButton dateUpdate;
	private InputField<NonnegativeInteger> numberOfDaysField;
	private InputField<Boolean> takeTodayCheckBox;
	private InputField<Boolean> randomiseCheckBox;
	private InputField<Boolean> takeDefaultCheckBox;
	private JButton defaultSettings;
	private JButton giveProposal;

	private Locale currentLocale = Locale.getDefault();
	private ResourceBundle messages;

	public ProposalSummary(DataStore mealPlan, JFrame parentFrame, Locale parentLocale,
			ResourceBundle parentMes) {
		this.mealPlan = mealPlan;
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new GridLayout(0, 2));
		this.currentLocale = parentLocale;
		this.messages = parentMes;

		mealPlan.register(this);
	}

	public JPanel buildProposalPanel(ActionListener updateMeals, ActionListener setDefaultSettings,
			ActionListener makeProposal) {
		buildDateShowField(mealPlan.getTime());
		buildInputFields();
		buildButtons(messages, updateMeals, setDefaultSettings, makeProposal);

		adjustFieldsOnPanel();

		if (mealPlan.getDaysPassed() == 0) {
			dateUpdate.setEnabled(false);
			giveProposal.setEnabled(true);
		} else {
			dateUpdate.setEnabled(true);
			giveProposal.setEnabled(false);
		}

		return dataPanel;
	}

	private void buildDateShowField(Date lastDate) {
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, currentLocale);
		dateShow = new JLabel(
				messages.getString("dateLastUpdate") + " " + dateFormat.format(lastDate));
	}

	private void buildButtons(ResourceBundle parentMes, ActionListener updateMeals,
			ActionListener setDefaultSettings, ActionListener makeProposal) {
		dateUpdate = SwingUtilityMethods.createButton(parentMes, "updateButton",
				"updateButtonMnemonic", action -> {
					updateMeals.actionPerformed(action);
					update();
				});
		defaultSettings = SwingUtilityMethods.createButton(parentMes, "proposalDefaultButton",
				"proposalDefaultButtonMnemonic", setDefaultSettings);
		giveProposal = SwingUtilityMethods.createButton(parentMes, "proposalShowButton",
				"proposalShowButtonMnemonic", makeProposal);
	}

	private void buildInputFields() {
		numberOfDaysField = new NonnegativeIntegerInputField(messages.getString("proposalDays"),
				new NonnegativeInteger(7));
		takeTodayCheckBox = new CheckboxInputField(messages.getString("proposalTake"));
		randomiseCheckBox = new CheckboxInputField(messages.getString("proposalRandom"));
		takeDefaultCheckBox = new CheckboxInputField(messages.getString("proposalDefault"));
	}

	private void adjustFieldsOnPanel() {
		dataPanel.add(dateShow);
		dataPanel.add(dateUpdate);
		numberOfDaysField.addToPanel(dataPanel);
		takeTodayCheckBox.addToPanel(dataPanel);
		randomiseCheckBox.addToPanel(dataPanel);
		takeDefaultCheckBox.addToPanel(dataPanel);
		dataPanel.add(defaultSettings);
		dataPanel.add(giveProposal);
	}

	public ProposalOutline getProposalOutline() {
		int numberOfDays = numberOfDaysField.getUserInput().value;
		boolean shallBeRandomised = randomiseCheckBox.getUserInput();
		boolean includesToday = takeTodayCheckBox.getUserInput();

		return new ProposalOutline(numberOfDays, includesToday, shallBeRandomised,
				mealPlan.getTime());
	}

	public void update() {
		Date lastDate = mealPlan.getTime();
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG, currentLocale);
		dateShow.setText(messages.getString("dateLastUpdate") + " " + dateFormat.format(lastDate));
		if (mealPlan.getDaysPassed() == 0) {
			dateUpdate.setEnabled(false);
			giveProposal.setEnabled(true);
		}
	}

	@Override
	public void updateData(DataStoreEventType event) {
		if (event == DataStoreEventType.DATE_UPDATED) {
			update();
		}
	}
}