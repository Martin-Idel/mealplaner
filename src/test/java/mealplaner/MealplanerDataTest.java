package mealplaner;

import static java.util.Optional.empty;
import static mealplaner.DataStoreEventType.DATABASE_EDITED;
import static mealplaner.DataStoreEventType.DATE_UPDATED;
import static mealplaner.DataStoreEventType.PROPOSAL_ADDED;
import static mealplaner.DataStoreEventType.SETTINGS_CHANGED;
import static mealplaner.MealplanerData.generateXml;
import static mealplaner.MealplanerData.readXml;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static testcommons.MealAssert.assertThat;

import java.util.ArrayList;
import java.util.Calendar;
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
import mealplaner.model.MealplanerCalendar;
import mealplaner.model.Proposal;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.Settings;

@RunWith(MockitoJUnitRunner.class)
public class MealplanerDataTest {
	private List<Meal> meals = new ArrayList<>();
	private Meal meal1;
	private Meal meal2;
	private Meal meal3;
	private Meal meal4;
	private DataStoreListener listener;
	@Mock
	private MealplanerCalendar cal;
	@Mock
	private Proposal proposal;

	private MealplanerData sut;

	@Before
	public void setUp() {
		addInitializedMeals();
		when(cal.updateCalendar()).thenReturn(10);
		when(proposal.getProposalList()).thenReturn(new ArrayList<>());
		Settings[] defaultSettings = getCorrectlyFilledSettings();
		sut = new MealplanerData(meals, cal, defaultSettings, proposal);

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

		sut.update(proposal.getProposalList());

		assertThat(meal1.getDaysPassed()).isEqualByComparingTo(60);
		assertThat(meal2.getDaysPassed()).isEqualByComparingTo(111);
		assertThat(meal4.getDaysPassed()).isEqualByComparingTo(30);
	}

	@Test
	public void updateMealCorrectlyUpdatesCookedMeals() {
		List<Meal> proposalMeals = new ArrayList<>();
		proposalMeals.add(meal1);
		proposalMeals.add(meal4);
		when(cal.updateCalendar()).thenReturn(2);
		when(proposal.getProposalList()).thenReturn(proposalMeals);
		sut = new MealplanerData(meals, cal, getCorrectlyFilledSettings(), proposal);

		sut.update(proposal.getProposalList());

		assertThat(meal1.getDaysPassed()).isEqualByComparingTo(1);
		assertThat(meal2.getDaysPassed()).isEqualByComparingTo(103);
		assertThat(meal4.getDaysPassed()).isEqualByComparingTo(0);
	}

	@Test
	public void updateMealNotifiesCorrectListeners() {

		sut.update(proposal.getProposalList());

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

		sut.setDefaultSettings(getCorrectlyFilledSettings());

		verify(listener).updateData(SETTINGS_CHANGED);
	}

	@Test(expected = MealException.class)
	public void setDefaultSettingsThrowsExceptionIfSettingsHaveWrongSize() {
		Settings[] defaultSettings = new Settings[6];
		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = new Settings();
		}
		sut.setDefaultSettings(defaultSettings);
	}

	@Test(expected = MealException.class)
	public void setDefaultSettingsThrowsExceptionIfSomeSettingsAreNull() {
		sut.setDefaultSettings(new Settings[7]);
	}

	@Test
	public void setDateNotifiesCorrectListeners() {

		sut.setDate(0, 0, 0);

		verify(listener).updateData(DATE_UPDATED);
	}

	@Test
	public void mealplanerXmlSaving() throws ParserConfigurationException {
		Settings[] defaultSettings = getCorrectlyFilledSettings();
		Proposal lastProposal = new Proposal();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(2000000000);
		MealplanerCalendar mealplanerCalendar = new MealplanerCalendar(cal);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		sut = new MealplanerData(meals, mealplanerCalendar, defaultSettings, lastProposal);

		MealplanerData actual = readXml(generateXml(saveFileContent, sut));
		assertThat(actual.getMeals()).asList().containsAll(sut.getMeals());
		assertThat(actual.getDaysPassed()).isEqualByComparingTo(sut.getDaysPassed());
		assertThat(actual.getLastProposal()).isEqualTo(sut.getLastProposal());
	}

	private void addInitializedMeals() throws MealException {
		meal1 = new Meal("Meal1", CookingTime.SHORT, Sidedish.NONE, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 50, "", empty());
		meals.add(meal1);
		meal2 = new Meal("Meal2", CookingTime.MEDIUM, Sidedish.PASTA,
				ObligatoryUtensil.CASSEROLE, CookingPreference.RARE, 101, "", empty());
		meals.add(meal2);
		meal4 = new Meal("Meal4", CookingTime.LONG, Sidedish.RICE, ObligatoryUtensil.POT,
				CookingPreference.VERY_POPULAR, 20, "", empty());
		meals.add(meal4);
	}

	private Meal initializeNewMeal() {
		return new Meal("Meal3", CookingTime.SHORT, Sidedish.POTATOES, ObligatoryUtensil.PAN,
				CookingPreference.NO_PREFERENCE, 10, "", empty());
	}

	private Settings[] getCorrectlyFilledSettings() {
		Settings[] defaultSettings = new Settings[7];
		for (int i = 0; i < defaultSettings.length; i++) {
			defaultSettings[i] = new Settings();
		}
		return defaultSettings;
	}
}
