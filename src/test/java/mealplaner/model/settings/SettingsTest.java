package mealplaner.model.settings;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static java.time.DayOfWeek.valueOf;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.model.enums.CookingTime.VERY_SHORT;
import static mealplaner.model.settings.CookingTimeSetting.cookingTimeWithProhibited;
import static mealplaner.model.settings.Settings.from;
import static mealplaner.model.settings.Settings.parseSettings;
import static mealplaner.model.settings.Settings.writeSettings;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getSettings1;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;
import testcommons.BundlesInitialization;

public class SettingsTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	private Settings sut;

	@Test
	public void saveAndReadFromXmlNode() throws ParserConfigurationException {
		Settings settings = getSettings1();
		Document saveFileContent = createDocument();

		sut = parseSettings(writeSettings(saveFileContent, settings, MONDAY, "setting"));

		assertThat(sut).isEqualTo(settings);
	}

	@Test
	public void canReadDayOfWeekFromAttributes() throws ParserConfigurationException {
		Settings settings = getSettings1();
		Document saveFileContent = createDocument();

		Element testElement = Settings.writeSettings(saveFileContent, settings, WEDNESDAY,
				"setting");

		assertThat(WEDNESDAY).isEqualTo(valueOf(
				testElement.getAttributes().getNamedItem("dayOfWeek").getTextContent()));
	}

	@Test
	public void readFromXmlNode() throws ParserConfigurationException {
		Settings settings = getSettings1();
		Document saveFileContent = createDocument();

		Element settingsNode = saveFileContent.createElement("setting");
		settingsNode.setAttribute("dayOfWeek", Integer.toString(1));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"casseroleSettings",
				() -> settings.getCasserole().name()));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"VERY_SHORT",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(VERY_SHORT))));

		sut = Settings.parseSettings(settingsNode);

		Settings expectedSettings = from(cookingTimeWithProhibited(VERY_SHORT),
				nonNegative(2),
				CasseroleSettings.NONE, PreferenceSettings.NORMAL);

		assertThat(sut).isEqualTo(expectedSettings);
	}
}
