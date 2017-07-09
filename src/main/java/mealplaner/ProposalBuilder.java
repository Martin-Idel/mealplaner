package mealplaner;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import mealplaner.model.Meal;
import mealplaner.model.MealListData;
import mealplaner.model.Proposal;
import mealplaner.model.SideDish;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

public class ProposalBuilder {

	private SideDish sideDish;
	private boolean firstDayIsToday;
	private boolean random;

	public ProposalBuilder() {
		this(new SideDish());
	}

	public ProposalBuilder(SideDish sideDish) {
		this.sideDish = sideDish;
	}

	public ProposalBuilder randomise(boolean randomise) {
		random = randomise;
		return this;
	}

	public ProposalBuilder firstProposal(boolean today) {
		firstDayIsToday = today;
		return this;
	}

	public Proposal propose(MealListData mealListData, Settings[] settings) {
		Proposal proposal = prepareProposal();
		mealListData.sortMealsAccordingToPriorities();
		setCurrentSideDishFromHistory(mealListData);
		if (mealListData.isNotEmpty()) {
			for (int today = 0; today < settings.length; today++) {
				updateListIncludingNewProposals(mealListData, proposal, today);
				Meal nextProposal = calculateNextDish(mealListData, settings[today], random);
				proposal.addItemToProposalList(nextProposal);
				updateCurrentSidedish(nextProposal);
			}
		}
		mealListData.sortMealsAccordingToName();
		return proposal;
	}

	public Proposal prepareProposal() {
		return new Proposal(Calendar.getInstance(), firstDayIsToday);
	}

	private void setCurrentSideDishFromHistory(MealListData mealListData) {
		sideDish.reset();
		Optional<List<Meal>> mealList = mealListData.getLastCookedDishes(3);
		if (mealList.isPresent()) {
			sideDish.current = mealList.get().get(0).getSidedish();
			while (sideDish.inARow < 3
					&& mealList.get().get(sideDish.inARow).getSidedish() == sideDish.current) {
				sideDish.incrementInARow();
			}
		}
	}

	private void updateListIncludingNewProposals(MealListData mealListData, Proposal proposal,
			int daysSinceStartOfProposal) {
		mealListData.setPriorityToDaysPassedPlus(daysSinceStartOfProposal);
		for (int j = 0; j < proposal.getSize(); j++) {
			mealListData.setPriorityOfMeal(proposal.getItem(j), daysSinceStartOfProposal - j);
		}
		mealListData.sortMealsAccordingToPriorities();
	}

	private Meal calculateNextDish(MealListData mealListData, Settings settings, boolean random) {
		prohibitMealsAccordingToSettings(mealListData, settings);
		takeYesterdaysSidedishIntoAccount(mealListData);
		setPriorityMultipliersAccordingToSetting(mealListData, settings.getPreference());
		if (random) {
			mealListData.addRandomIntegersToPriorities();
		}
		mealListData.sortMealsAccordingToPriorities();
		Meal nextProposal = mealListData.getMealWithHighestPriority();
		return nextProposal;
	}

	private void prohibitMealsAccordingToSettings(MealListData mealListData, Settings settings) {
		mealListData.prohibitMealWithSetting(settings.getCookingTime());
		mealListData.prohibitMealWithSetting(settings.getCookingUtensil());
		mealListData.prohibitMealWithSetting(settings.getCookingPreference());
	}

	private void takeYesterdaysSidedishIntoAccount(MealListData mealListData) {
		float sideDishMultiplier = (3f - sideDish.inARow) / 2;
		mealListData.setSidedishMultipliers(sideDish.current, sideDishMultiplier);
	}

	// TODO: Here we just want to hand filters according to settings to the
	// mealListData.
	// Maybe we won't need it anymore at some time.
	private void setPriorityMultipliersAccordingToSetting(MealListData mealListData,
			PreferenceSettings preferenceSetting) {
		if (preferenceSetting == PreferenceSettings.NORMAL
				|| preferenceSetting == PreferenceSettings.RARE_NONE) {
			mealListData.setPreferenceMutlipliers(CookingPreference.VERY_POPULAR, 4);
			mealListData.setPreferenceMutlipliers(CookingPreference.NO_PREFERENCE, 2);
		} else if (preferenceSetting == PreferenceSettings.RARE_PREFERED) {
			mealListData.setPreferenceMutlipliers(CookingPreference.RARE, 2);
		}
	}

	private void updateCurrentSidedish(Meal nextProposal) {
		sideDish = (sideDish.current == nextProposal.getSidedish())
				? sideDish.incrementInARow()
				: sideDish.resetToSideDish(nextProposal.getSidedish());
	}
}