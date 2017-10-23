package mealplaner.gui;

import static java.time.format.DateTimeFormatter.ofLocalizedDate;
import static java.time.format.FormatStyle.LONG;
import static mealplaner.gui.commons.SwingUtilityMethods.createButton;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mealplaner.BundleStore;
import mealplaner.DataStore;
import mealplaner.DataStoreEventType;
import mealplaner.DataStoreListener;
import mealplaner.commons.NonnegativeInteger;
import mealplaner.gui.commons.CheckboxInputField;
import mealplaner.gui.commons.InputField;
import mealplaner.gui.commons.NonnegativeIntegerInputField;
import mealplaner.gui.commons.SwingUtilityMethods;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.ProposalOutline.ProposalOutlineBuilder;

public class ProposalSummary implements DataStoreListener {
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

	private BundleStore bundles;

	public ProposalSummary(DataStore mealPlan, JFrame parentFrame, BundleStore bundles) {
		this.mealPlan = mealPlan;
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new GridLayout(0, 2));
		this.bundles = bundles;

		mealPlan.register(this);
	}

	public JPanel buildProposalPanel(ActionListener updateMeals, ActionListener setDefaultSettings,
			ActionListener makeProposal) {
		buildDateShowField(mealPlan.getTime());
		buildInputFields();
		buildButtons(bundles, updateMeals, setDefaultSettings, makeProposal);

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

	private void buildDateShowField(LocalDate lastDate) {
		String formattedLastDate = lastDate
				.format(ofLocalizedDate(LONG).withLocale(bundles.locale()));
		dateShow = new JLabel(bundles.message("updatedLastDate") + " " + formattedLastDate);
	}

	private void buildButtons(BundleStore bundles, ActionListener updateMeals,
			ActionListener setDefaultSettings, ActionListener makeProposal) {
		dateUpdate = createButton(bundles.message("updateButton"),
				bundles.message("updateButtonMnemonic"),
				action -> {
					updateMeals.actionPerformed(action);
					update();
				});
		defaultSettings = SwingUtilityMethods.createButton(
				bundles.message("proposalChangeDefaultSettingsButton"),
				bundles.message("proposalChangeDefaultSettingsButtonMnemonic"),
				setDefaultSettings);
		giveProposal = SwingUtilityMethods.createButton(bundles.message("proposalShowButton"),
				bundles.message("proposalShowButtonMnemonic"), makeProposal);
	}

	private void buildInputFields() {
		numberOfDaysField = new NonnegativeIntegerInputField(
				bundles.message("proposalNumberOfDays"), new NonnegativeInteger(7));
		takeTodayCheckBox = new CheckboxInputField(bundles.message("proposalStartToday"));
		randomiseCheckBox = new CheckboxInputField(bundles.message("proposalRandomize"));
		takeDefaultCheckBox = new CheckboxInputField(
				bundles.message("proposalApplyDefaultSettings"));
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
		ProposalOutlineBuilder builder = ProposalOutline.ProposalOutlineBuilder
				.of(numberOfDaysField.getUserInput().value, mealPlan.getTime());
		if (randomiseCheckBox.getUserInput()) {
			builder.randomise();
		}
		if (takeTodayCheckBox.getUserInput()) {
			builder.includeToday();
		}

		return builder.build();
	}

	public void update() {
		String formattedLastDate = mealPlan.getTime()
				.format(ofLocalizedDate(LONG).withLocale(bundles.locale()));
		dateShow.setText(bundles.message("updatedLastDate") + " " + formattedLastDate);
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