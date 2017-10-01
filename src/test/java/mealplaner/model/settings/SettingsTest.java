package mealplaner.model.settings;

import static mealplaner.model.enums.CookingTime.SHORT;
import static mealplaner.model.enums.CookingTime.VERY_SHORT;
import static org.junit.Assert.assertEquals;

import java.util.function.Supplier;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;

public class SettingsTest {

	private Settings sut;

	@Test
	public void saveAndReadFromXmlNode() throws ParserConfigurationException {
		Settings settings = new Settings(new CookingTimeSetting(CookingTime.VERY_SHORT), false,
				CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		sut = Settings.loadFromXml(Settings.generateXml(saveFileContent, settings, 0));

		compareTwoSettings(settings, sut);
	}

	@Test
	public void canReadDayOfWeekFromAttributes() throws ParserConfigurationException {
		Settings settings = new Settings(new CookingTimeSetting(CookingTime.VERY_SHORT), false,
				CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();

		Element testElement = Settings.generateXml(saveFileContent, settings, 3);

		assertEquals("3", testElement.getAttributes().getNamedItem("dayOfWeek").getTextContent());
	}

	@Test
	public void readFromXmlNode() throws ParserConfigurationException {
		Settings settings = new Settings(new CookingTimeSetting(VERY_SHORT, SHORT), false,
				CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		Document saveFileContent = documentBuilder.newDocument();
		Element settingsNode = saveFileContent.createElement("setting");
		settingsNode.setAttribute("dayOfWeek", Integer.toString(1));
		settingsNode.appendChild(node(saveFileContent,
				"casseroleSettings",
				() -> settings.getCasserole().name()));
		settingsNode.appendChild(node(saveFileContent,
				"VERY_SHORT",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(VERY_SHORT))));

		sut = Settings.loadFromXml(settingsNode);

		Settings expectedSettings = new Settings(new CookingTimeSetting(VERY_SHORT),
				false,
				CasseroleSettings.NONE, PreferenceSettings.NORMAL);

		compareTwoSettings(expectedSettings, sut);
	}

	private static Element node(Document doc, String name,
			Supplier<String> stringRepresentationOfField) {
		Element mealFieldNode = doc.createElement(name);
		mealFieldNode.appendChild(doc.createTextNode(stringRepresentationOfField.get()));
		return mealFieldNode;
	}

	private void compareTwoSettings(Settings expected, Settings actual) {
		assertEquals(expected.getCasserole(), actual.getCasserole());
		assertEquals(expected.getPreference(), actual.getPreference());
		assertEquals(expected.getCookingTime().isTimeProhibited(CookingTime.VERY_SHORT),
				actual.getCookingTime().isTimeProhibited(CookingTime.VERY_SHORT));
		assertEquals(expected.getCookingTime().isTimeProhibited(CookingTime.SHORT),
				actual.getCookingTime().isTimeProhibited(CookingTime.SHORT));
		assertEquals(expected.getCookingTime().isTimeProhibited(CookingTime.MEDIUM),
				actual.getCookingTime().isTimeProhibited(CookingTime.MEDIUM));
		assertEquals(expected.getCookingTime().isTimeProhibited(CookingTime.LONG),
				actual.getCookingTime().isTimeProhibited(CookingTime.LONG));
		assertEquals(expected.getCookingUtensil().isUtensilProhibited(ObligatoryUtensil.PAN),
				actual.getCookingUtensil().isUtensilProhibited(ObligatoryUtensil.PAN));
	}
}
