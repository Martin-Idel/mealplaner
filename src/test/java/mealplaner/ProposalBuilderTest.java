package mealplaner;

import static java.util.Optional.empty;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.Meal.createMeal;
import static mealplaner.model.enums.CookingPreference.RARE;
import static mealplaner.model.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.CookingTimeSetting.defaultCookingTime;
import static mealplaner.model.settings.Settings.from;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import mealplaner.commons.Pair;
import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.SideDish;
import mealplaner.model.configuration.PreferenceMap;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.Settings;
import testcommons.BundlesInitialization;

public class ProposalBuilderTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	private List<Meal> meals;
	private ProposalBuilder proposalBuilder;
	private SideDish sideDish;
	private Settings[] settings;

	@Before
	public void setup() {
		meals = new ArrayList<>();
		sideDish = new SideDish();
		proposalBuilder = new ProposalBuilder(PreferenceMap.getPreferenceMap(), sideDish);
		settings = new Settings[1];
	}

	@Test
	public void proposeNoShortManyPeopleRestInactive() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = cookingTimeWithProhibited(CookingTime.SHORT);
		settings[0] = from(cookingTimeSetting, nonNegative(4),
				CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertEquals(1, proposal.getProposalList().size());
		assertMealsEquals(meals.get(1), proposal.getItem(0));
	}

	@Test
	public void proposeOnlyVeryPopoularNoCasserole() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2),
				CasseroleSettings.NONE, PreferenceSettings.VERY_POPULAR_ONLY);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(2), proposal.getItem(0));
	}

	@Test
	public void proposeOnlyCasseroleRareNone() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2),
				CasseroleSettings.ONLY, PreferenceSettings.RARE_NONE);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(3), proposal.getItem(0));
	}

	@Test
	public void proposeSideDishMultiplierTwo() throws MealException {
		addMealsToTestMultipliers();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(1),
				CasseroleSettings.POSSIBLE, PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(2), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierRare() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
				PreferenceSettings.RARE_PREFERED);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(1), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierNormal() throws MealException {
		addMeals();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(4), proposal.getItem(0));
	}

	@Test
	public void proposePreferenceMultiplierRarePreferredButCookedRecently()
			throws MealException {
		addMealsToTestMultipliers();
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
				PreferenceSettings.RARE_PREFERED);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(4), proposal.getItem(0));
	}

	@Test
	public void multipliersCanBeConfiguredByDifferentHashMap() {
		addMealsToTestMultipliers();
		HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap = new HashMap<>();
		preferenceMap.put(Pair.of(RARE, NORMAL), 10);
		CookingTimeSetting cookingTimeSetting = defaultCookingTime();
		settings[0] = from(cookingTimeSetting, nonNegative(2), CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);
		proposalBuilder = new ProposalBuilder(preferenceMap, sideDish);

		Proposal proposal = proposalBuilder.propose(meals, settings);

		assertMealsEquals(meals.get(1), proposal.getItem(0));
	}

	private void addMeals() throws MealException {
		Meal meal1 = createMeal("Meal1", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, nonNegative(50), "", empty());
		meals.add(meal1);
		Meal meal2 = createMeal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
				ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, nonNegative(101), "", empty());
		meals.add(meal2);
		Meal meal3 = createMeal("Meal3", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, nonNegative(20), "", empty());
		meals.add(meal3);
		Meal meal4 = createMeal("Meal4", CookingTime.MEDIUM, Sidedish.POTATOES,
				ObligatoryUtensil.CASSEROLE, CookingPreference.VERY_POPULAR, nonNegative(25), "",
				empty());
		meals.add(meal4);
		Meal meal5 = createMeal("Meal5", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, nonNegative(100), "", empty());
		meals.add(meal5);
	}

	private void addMealsToTestMultipliers() throws MealException {
		Meal meal1 = createMeal("Meal1", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, nonNegative(0), "", empty());
		meals.add(meal1);
		Meal meal2 = createMeal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
				ObligatoryUtensil.CASSEROLE,
				CookingPreference.RARE, nonNegative(10), "", empty());
		meals.add(meal2);
		Meal meal3 = createMeal("Meal3", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, nonNegative(20), "", empty());
		meals.add(meal3);
		Meal meal4 = createMeal("Meal4", CookingTime.MEDIUM, Sidedish.POTATOES,
				ObligatoryUtensil.CASSEROLE,
				CookingPreference.NO_PREFERENCE, nonNegative(30), "", empty());
		meals.add(meal4);
		Meal meal5 = createMeal("Meal5", CookingTime.SHORT, Sidedish.PASTA, ObligatoryUtensil.POT,
				CookingPreference.NO_PREFERENCE, nonNegative(70), "", empty());
		meals.add(meal5);
	}

	public static void assertMealsEquals(Meal expected, Meal actual) {
		assertEquals(expected.getName(), actual.getName());
	}
}
