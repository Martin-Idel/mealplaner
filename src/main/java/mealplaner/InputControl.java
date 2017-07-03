package mealplaner;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import mealplaner.gui.ProposalSummary;
import mealplaner.gui.dialogs.pastupdate.UpdatePastMeals;
import mealplaner.gui.dialogs.proposaloutput.ProposalOutput;
import mealplaner.gui.dialogs.settingsinput.DefaultSettingsInput;
import mealplaner.gui.dialogs.settingsinput.ProposalSettingsInput;
import mealplaner.gui.dialogs.settingsinput.SettingsInput;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.settings.ProposalOutline;
import mealplaner.model.settings.Settings;

public class InputControl {
	private JFrame dataFrame;
	private MealplanerData mealPlan;

	private ProposalSummary proposalSummary;

	private ResourceBundle messages;
	private Locale currentLocale;

	public InputControl(ResourceBundle messages, MealplanerData mealplaner, ProposalBuilder proposalBuilder) {
		this.messages = messages;
		this.mealPlan = mealplaner;
	}

	public void makeProposal() {
		ProposalOutline outline = proposalSummary.getProposalOutline();

		if (outline.isIncludedToday()) {
			Settings[] settings = setDefaultSettings(outline);
			Proposal proposal = propose(settings, outline.isIncludedToday(), outline.isShallBeRandomised());
			mealPlan.setLastProposal(proposal);
			new ProposalOutput(dataFrame, mealPlan.getLastProposal(), currentLocale, messages);
		} else {
			SettingsInput input = new ProposalSettingsInput(dataFrame, mealPlan.getDefaultSettings(),
					outline, messages, currentLocale);
			input.showDialog()
					.map(settings -> propose(settings, outline.isIncludedToday(), outline.isShallBeRandomised()))
					.ifPresent(proposal -> {
						mealPlan.setLastProposal(proposal);
						new ProposalOutput(dataFrame, mealPlan.getLastProposal(), currentLocale, messages);
					});
		}
	}

	private Settings[] setDefaultSettings(ProposalOutline outline) {
		Settings[] settings = new Settings[outline.getNumberOfDays()];
		int dayOfWeek = mealPlan.getCalendar().get(Calendar.DAY_OF_WEEK); // today
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

	private Proposal propose(Settings[] set, boolean today, boolean random) {
		ProposalBuilder proposalBuilder = new ProposalBuilder();
		if (today) {
			proposalBuilder.firstProposalToday();
		}
		if (random) {
			proposalBuilder.randomiseProposal();
		}
		return proposalBuilder.propose(mealPlan.getMealListData(), set);
	}

	public void changeDefaultSettings() {
		SettingsInput defaultSettingInput = new DefaultSettingsInput(dataFrame,
				mealPlan.getDefaultSettings(),
				messages);
		Optional<Settings[]> defaultSettings = defaultSettingInput.showDialog();
		defaultSettings.ifPresent(settings -> mealPlan.setDefaultSettings(settings));
	}

	public void updatePastMeals() {
		UpdatePastMeals updateMeals = new UpdatePastMeals(dataFrame, mealPlan, currentLocale, messages);
		List<Meal> lastCookedMealList = updateMeals.showDialog();
		mealPlan.update(lastCookedMealList);
		proposalSummary.update();
	}

	public void updateDatabase() {

	}

	public void saveDatabase() {

	}

	public void save() {

	}

	public void load() {

	}
}
