package mealplaner;

import static java.lang.Integer.compare;
import static mealplaner.model.enums.CookingPreference.NO_PREFERENCE;
import static mealplaner.model.enums.CookingPreference.RARE;
import static mealplaner.model.enums.CookingPreference.VERY_POPULAR;
import static mealplaner.model.enums.PreferenceSettings.NORMAL;
import static mealplaner.model.enums.PreferenceSettings.RARE_NONE;
import static mealplaner.model.enums.PreferenceSettings.RARE_PREFERED;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import mealplaner.commons.Pair;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.SideDish;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.settings.Settings;

public class ProposalBuilder {

	private SideDish sideDish;
	private boolean firstDayIsToday;
	private boolean random;

	private final HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap;
	private final Random randomIntGenerator = new Random();

	public ProposalBuilder() {
		this(new SideDish());
	}

	public ProposalBuilder(SideDish sideDish) {
		this.sideDish = sideDish;

		HashMap<Pair<CookingPreference, PreferenceSettings>, Integer> preferenceMap = new HashMap<>();
		preferenceMap.put(Pair.of(VERY_POPULAR, NORMAL), 4);
		preferenceMap.put(Pair.of(VERY_POPULAR, RARE_NONE), 4);
		preferenceMap.put(Pair.of(NO_PREFERENCE, NORMAL), 2);
		preferenceMap.put(Pair.of(NO_PREFERENCE, RARE_NONE), 2);
		preferenceMap.put(Pair.of(RARE, RARE_PREFERED), 2);
		this.preferenceMap = preferenceMap;
	}

	public ProposalBuilder randomise(boolean randomise) {
		random = randomise;
		return this;
	}

	public ProposalBuilder firstProposal(boolean today) {
		firstDayIsToday = today;
		return this;
	}

	public Proposal propose(List<Meal> meals, Settings[] settings) {
		Proposal proposal = prepareProposal();
		setCurrentSideDishFromHistory(meals);
		if (!meals.isEmpty()) {
			for (int today = 0; today < settings.length; today++) {
				Optional<Meal> nextMeal = proposeNextMeal(meals, proposal, settings[today]);
				proposal.addItemToProposalList(nextMeal.orElse(meals.get(0)));
				updateCurrentSidedish(nextMeal.orElse(meals.get(0)));
			}
		}
		return proposal;
	}

	public Proposal prepareProposal() {
		return new Proposal(Calendar.getInstance(), firstDayIsToday);
	}

	private void setCurrentSideDishFromHistory(List<Meal> meals) {
		sideDish.reset();
		List<Meal> mealList = getLastCookedDishes(meals);
		if (!mealList.isEmpty()) {
			sideDish.current = mealList.get(0).getSidedish();
			while (sideDish.inARow < 3
					&& mealList.get(sideDish.inARow).getSidedish() == sideDish.current) {
				sideDish.incrementInARow();
			}
		}
	}

	private List<Meal> getLastCookedDishes(List<Meal> meals) {
		return meals.stream()
				.sorted((meal1, meal2) -> compare(meal1.getDaysPassed(), meal2.getDaysPassed()))
				.limit(3)
				.collect(Collectors.toList());
	}

	private void updateCurrentSidedish(Meal nextProposal) {
		sideDish = (sideDish.current == nextProposal.getSidedish())
				? sideDish.incrementInARow()
				: sideDish.resetToSideDish(nextProposal.getSidedish());
	}

	public Optional<Meal> proposeNextMeal(
			List<Meal> meals,
			final Proposal proposal,
			final Settings settings) {

		return meals.stream()
				.filter(meal -> allows(meal, settings))
				.map(meal -> Pair.of(meal, meal.getDaysPassed()))
				.map(pair -> takeProposalIntoAccount(pair, proposal))
				.map(pair -> takeSidedishIntoAccount(pair, sideDish))
				.map(pair -> multiplyPrefs(pair, settings.getPreference()))
				.map(pair -> randomize(pair))
				.sorted((pair1, pair2) -> -(pair1.right.compareTo(pair2.right)))
				.map(pair -> pair.left)
				.findFirst();
	}

	private Pair<Meal, Integer> takeProposalIntoAccount(Pair<Meal, Integer> pair,
			Proposal proposal) {

		List<Meal> proposalList = proposal.getProposalList();
		return proposalList.contains(pair.left)
				? Pair.of(pair.left, proposalList.size() - proposalList.indexOf(pair.left) - 1)
				: pair;
	}

	private Pair<Meal, Integer> takeSidedishIntoAccount(Pair<Meal, Integer> pair,
			SideDish sideDish) {
		return (pair.left.getSidedish() == sideDish.current)
				? Pair.of(pair.left, (int) (pair.right * (3f - sideDish.inARow) / 2))
				: pair;
	}

	private Pair<Meal, Integer> randomize(Pair<Meal, Integer> pair) {
		return random ? Pair.of(pair.left, pair.right + randomIntGenerator.nextInt(7)) : pair;
	}

	private Pair<Meal, Integer> multiplyPrefs(Pair<Meal, Integer> pair,
			PreferenceSettings preferenceSetting) {

		Pair<CookingPreference, PreferenceSettings> currentSettings = Pair
				.of(pair.left.getCookingPreference(), preferenceSetting);
		return preferenceMap.containsKey(currentSettings)
				? Pair.of(pair.left, pair.right * preferenceMap.get(currentSettings))
				: pair;
	}

	private boolean allows(Meal meal, Settings settings) {
		return !(settings.getCookingTime().prohibits(meal)
				|| settings.getCookingUtensil().prohibits(meal)
				|| settings.getCookingPreference().prohibits(meal));
	}
}