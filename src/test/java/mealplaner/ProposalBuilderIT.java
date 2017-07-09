package mealplaner;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.MealListData;
import mealplaner.model.Proposal;
import mealplaner.model.SideDish;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.Settings;

public class ProposalBuilderIT {

	private ArrayList<Meal> mealList;
	private MealListData mealListData;
	private ProposalBuilder proposalBuilder;
	private SideDish sideDish;
	private Settings[] settings;

	@Before
	public void setup() {
		mealList = new ArrayList<>();
		mealListData = new MealListData(mealList);
		sideDish = new SideDish();
		proposalBuilder = new ProposalBuilder(sideDish);
		settings = new Settings[1];
	}

	@Test
	public void proposeNoShortManyPeopleRestInactive() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting(CookingTime.SHORT);
		settings[0] = new Settings(cookingTimeSetting, true, CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertEquals(1, proposal.getProposalList().size());
		assertMealsEquals(mealList.get(1), proposal.getItem(0));
	}

	@Test
	public void proposeOnlyVeryPopoularNoCasserole() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.NONE,
				PreferenceSettings.VERY_POPULAR_ONLY);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(2), proposal.getItem(0));
	}

	@Test
	public void proposeOnlyCasseroleRareNone() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.ONLY,
				PreferenceSettings.RARE_NONE);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(3), proposal.getItem(0));
	}

	@Test
	public void proposeSideDishMultiplierTwo() throws MealException {
		addMealsToTestMultipliers();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(2), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierRare() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.RARE_PREFERED);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(1), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierNormal() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(4), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierRarePreferredButCookedRecently() throws MealException {
		addMealsToTestMultipliers();
		CookingTimeSetting cookingTimeSetting = new CookingTimeSetting();
		settings[0] = new Settings(cookingTimeSetting, false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.RARE_PREFERED);

		Proposal proposal = proposalBuilder.propose(mealListData, settings);

		assertMealsEquals(mealList.get(4), proposal.getItem(0));
	}

	private void addMeals() throws MealException {
		Meal meal1 = new Meal("Meal1", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 50);
		mealList.add(meal1);
		Meal meal2 = new Meal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
				CookingPreference.RARE, 100);
		mealList.add(meal2);
		Meal meal3 = new Meal("Meal3", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, 20);
		mealList.add(meal3);
		Meal meal4 = new Meal("Meal4", CookingTime.MEDIUM, Sidedish.POTATOES, ObligatoryUtensil.CASSEROLE,
				CookingPreference.VERY_POPULAR, 25);
		mealList.add(meal4);
		Meal meal5 = new Meal("Meal5", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, 100);
		mealList.add(meal5);
	}

	private void addMealsToTestMultipliers() throws MealException {
		Meal meal1 = new Meal("Meal1", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 0);
		mealList.add(meal1);
		Meal meal2 = new Meal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA, ObligatoryUtensil.CASSEROLE,
				CookingPreference.RARE, 10);
		mealList.add(meal2);
		Meal meal3 = new Meal("Meal3", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, 20);
		mealList.add(meal3);
		Meal meal4 = new Meal("Meal4", CookingTime.MEDIUM, Sidedish.POTATOES, ObligatoryUtensil.CASSEROLE,
				CookingPreference.NO_PREFERENCE, 30);
		mealList.add(meal4);
		Meal meal5 = new Meal("Meal5", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, 70);
		mealList.add(meal5);
	}

	public static void assertMealsEquals(Meal expected, Meal actual) {
		assertEquals(expected.getName(), actual.getName());
	}
}