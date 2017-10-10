package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.model.enums.CookingTime.VERY_SHORT;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getSettings1;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.PreferenceSettings;

public class SettingsTest {

	private Settings sut;

	@Test
	public void saveAndReadFromXmlNode() throws ParserConfigurationException {
		Settings settings = getSettings1();
		Document saveFileContent = createDocument();

		sut = Settings.loadFromXml(Settings.generateXml(saveFileContent, settings, 0, "setting"));

		assertThat(sut).isEqualTo(settings);
	}

	@Test
	public void canReadDayOfWeekFromAttributes() throws ParserConfigurationException {
		Settings settings = getSettings1();
		Document saveFileContent = createDocument();

		Element testElement = Settings.generateXml(saveFileContent, settings, 3, "setting");

		assertThat("3")
				.isEqualTo(testElement.getAttributes().getNamedItem("dayOfWeek").getTextContent());
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

		sut = Settings.loadFromXml(settingsNode);

		Settings expectedSettings = new Settings(new CookingTimeSetting(VERY_SHORT), nonNegative(2),
				CasseroleSettings.NONE, PreferenceSettings.NORMAL);

		assertThat(sut).isEqualTo(expectedSettings);
	}
}
