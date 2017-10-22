package mealplaner;

import static java.time.LocalDate.of;
import static java.util.Optional.empty;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.MealplanerData.generateXml;
import static mealplaner.MealplanerData.readXml;
import static mealplaner.model.Meal.meal;
import static mealplaner.model.settings.DefaultSettings.defaultSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static testcommons.MealAssert.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;

import mealplaner.errorhandling.MealException;
import mealplaner.model.Meal;
import mealplaner.model.Proposal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;

@RunWith(MockitoJUnitRunner.class)
public class MealplanerDataTest {
	private List<Meal> meals = new ArrayList<>();
	private Meal meal1;
	private Meal meal2;
	private Meal meal3;
	private Meal meal4;
	private DataStoreListener listener;
	private LocalDate date;
	@Mock
	private Proposal proposal;

	private MealplanerData sut;

	@Before
	public void setUp() {
		addInitializedMeals();
		date = of(2017, 5, 7);
		when(proposal.getProposalList()).thenReturn(new ArrayList<>());
		sut = new MealplanerData(meals, date, defaultSettings(), proposal);

		listener = mock(DataStoreListener.class);
		sut.register(listener);

		meal3 = initializeNewMeal();
	}

	@Test
	public void addMealAtSortedPosition() {

		sut.addMeal(meal3);

		assertThat(meals).asList().hasSize(4);
		assertThat(meals.get(2)).isEqualTo(meal3);
	}

	@Test
	public void setMealsNotifiesCorrectListeners() {

		sut.setMeals(proposal.getProposalList());

		verify(listener).updateData(DATABASE_EDITED);
	}

	@Test
	public void addMealNotifiesCorrectListeners() {

		sut.addMeal(meal3);

		verify(listener).updateData(DATABASE_EDITED);
	}

	@Test
	public void updateMealCorrectlyAddsDaysToNonCookedMeals() {

		sut.update(proposal.getProposalList(), date.plusDays(10));

		List<Meal> mealList = sut.getMeals();
		assertThat(mealList.get(0).getDaysPassed()).isEqualByComparingTo(60);
		assertThat(mealList.get(1).getDaysPassed()).isEqualByComparingTo(111);
		assertThat(mealList.get(2).getDaysPassed()).isEqualByComparingTo(30);
	}

	@Test
	public void updateMealCorrectlyUpdatesCookedMeals() {
		List<Meal> proposalMeals = new ArrayList<>();
		proposalMeals.add(meal1);
		proposalMeals.add(meal4);
		when(proposal.getProposalList()).thenReturn(proposalMeals);
		sut = new MealplanerData(meals, date, defaultSettings(), proposal);

		sut.update(proposal.getProposalList(), date.plusDays(2));

		List<Meal> mealList = sut.getMeals();
		assertThat(mealList.get(0).getDaysPassed()).isEqualByComparingTo(1);
		assertThat(mealList.get(1).getDaysPassed()).isEqualByComparingTo(103);
		assertThat(mealList.get(2).getDaysPassed()).isEqualByComparingTo(0);
	}

	@Test
	public void updateMealNotifiesCorrectListeners() {

		sut.update(proposal.getProposalList(), date.plusDays(1));

		verify(listener).updateData(DATABASE_EDITED);
		verify(listener).updateData(DATE_UPDATED);
	}

	@Test
	public void setLastProposalNotifiesCorrectListeners() {

		sut.setLastProposal(proposal);

		verify(listener).updateData(PROPOSAL_ADDED);
	}

	@Test
	public void setDefaultSettingsNotifiesCorrectListeners() {

		sut.setDefaultSettings(defaultSettings());

		verify(listener).updateData(SETTINGS_CHANGED);
	}

	@Test
	public void mealplanerXmlSaving() throws ParserConfigurationException {
		Proposal lastProposal = new Proposal();
		LocalDate date = LocalDate.of(2017, 5, 17);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		sut = new MealplanerData(meals, date, defaultSettings(), lastProposal);

		MealplanerData actual = readXml(generateXml(saveFileContent, sut));
		assertThat(actual.getMeals()).asList().containsAll(sut.getMeals());
		assertThat(actual.getDaysPassed()).isEqualByComparingTo(sut.getDaysPassed());
		assertThat(actual.getLastProposal()).isEqualTo(sut.getLastProposal());
	}

	private void addInitializedMeals() throws MealException {
		meal1 = meal("Meal1", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 50, "", empty());
		meals.add(meal1);
		meal2 = meal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
				ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, 101, "", empty());
		meals.add(meal2);
		meal4 = meal("Meal4", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, 20, "", empty());
		meals.add(meal4);
	}

	private Meal initializeNewMeal() {
		return meal("Meal3", CookingTime.SHORT, Sidedish.POTATOES, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 10, "", empty());
	}
}
