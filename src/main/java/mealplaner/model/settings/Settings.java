package mealplaner.model.settings;

import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.readBoolean;
import static mealplaner.io.XMLHelpers.readEnum;
import static mealplaner.io.XMLHelpers.readInt;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.commons.NonnegativeInteger;
import mealplaner.errorhandling.Logger;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;

public class Settings {
	private final CasseroleSettings casseroleSettings;
	private final PreferenceSettings preference;
	private final CookingPreferenceSetting cookingPreferences;
	private final CookingTimeSetting cookingTime;
	private final CookingUtensilSetting cookingUtensil;
	private final NonnegativeInteger numberOfPeople;

	public Settings() {
		this(new CookingTimeSetting(), nonNegative(2), CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);
	}

	public Settings(CookingTimeSetting cookingTime,
			NonnegativeInteger numberOfPeople,
			CasseroleSettings casseroleSettings,
			PreferenceSettings preferenceSettings) {

		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingTime = cookingTime;
		this.cookingUtensil = new CookingUtensilSetting();
		this.numberOfPeople = numberOfPeople;
		this.cookingUtensil.setNumberOfPeople(numberOfPeople.value);
		this.casseroleSettings = casseroleSettings;
		this.cookingUtensil.setCasseroleSettings(casseroleSettings);

		this.preference = preferenceSettings;
		this.cookingPreferences.setCookingPreferences(preference);
	}

	public Settings(Settings setting) {
		this.casseroleSettings = setting.getCasserole();
		this.preference = setting.getPreference();
		this.numberOfPeople = setting.getNumberOfPeople();
		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingPreferences.setCookingPreferences(this.preference);
		this.cookingTime = new CookingTimeSetting(setting.getCookingTime());
		this.cookingUtensil = new CookingUtensilSetting(setting.getCookingUtensil());
	}

	public NonnegativeInteger getNumberOfPeople() {
		return numberOfPeople;
	}

	public CookingTimeSetting getCookingTime() {
		return cookingTime;
	}

	public boolean isTimeProhibited(CookingTime cookingTime) {
		return this.cookingTime.isTimeProhibited(cookingTime);
	}

	public boolean isTimePossible(CookingTime cookingTime) {
		return !isTimeProhibited(cookingTime);
	}

	public boolean shallManyPeopleEat() {
		return cookingUtensil.isUtensilProhibited(ObligatoryUtensil.PAN);
	}

	public CookingUtensilSetting getCookingUtensil() {
		return cookingUtensil;
	}

	public CookingPreferenceSetting getCookingPreference() {
		return cookingPreferences;
	}

	public CasseroleSettings getCasserole() {
		return casseroleSettings;
	}

	public PreferenceSettings getPreference() {
		return preference;
	}

	public static Element generateXml(Document saveFileContent, Settings settings, int dayOfWeek,
			String elementName) {
		Element settingsNode = saveFileContent.createElement(elementName);
		settingsNode.setAttribute("dayOfWeek", Integer.toString(dayOfWeek));

		settingsNode.appendChild(createTextNode(saveFileContent,
				"casseroleSettings",
				() -> settings.getCasserole().name()));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"preferenceSettings",
				() -> settings.getPreference().name()));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"numberOfPeople",
				() -> Integer.toString(settings.getNumberOfPeople().value)));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"VERY_SHORT",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(CookingTime.VERY_SHORT))));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"SHORT",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(CookingTime.SHORT))));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"MEDIUM",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(CookingTime.MEDIUM))));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"LONG",
				() -> Boolean.toString(
						settings.getCookingTime().isTimeProhibited(CookingTime.LONG))));
		return settingsNode;
	}

	public static Settings loadFromXml(Element currentSetting) {
		CasseroleSettings casseroleSettings = readEnum(CasseroleSettings.POSSIBLE,
				CasseroleSettings::valueOf, currentSetting, "casseroleSettings");
		PreferenceSettings preferenceSetting = readEnum(PreferenceSettings.NORMAL,
				PreferenceSettings::valueOf, currentSetting, "preferenceSettings");
		int numberOfPeople = readInt(2, currentSetting, "numberOfPeople");
		if (numberOfPeople < 0) {
			numberOfPeople = 1;
			Logger.logParsingError(
					String.format("The numberOfPeople of Setting " + currentSetting.toString()
							+ " contains a negative number."));
		}
		CookingTimeSetting cookingTimes = new CookingTimeSetting();
		if (readBoolean(false, currentSetting, "VERY_SHORT")) {
			cookingTimes.prohibitCookingTime(CookingTime.VERY_SHORT);
		}
		if (readBoolean(false, currentSetting, "SHORT")) {
			cookingTimes.prohibitCookingTime(CookingTime.SHORT);
		}
		if (readBoolean(false, currentSetting, "MEDIUM")) {
			cookingTimes.prohibitCookingTime(CookingTime.MEDIUM);
		}
		if (readBoolean(false, currentSetting, "LONG")) {
			cookingTimes.prohibitCookingTime(CookingTime.LONG);
		}
		return new Settings(cookingTimes, nonNegative(numberOfPeople), casseroleSettings,
				preferenceSetting);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((casseroleSettings == null) ? 0 : casseroleSettings.hashCode());
		result = prime * result
				+ ((cookingPreferences == null) ? 0 : cookingPreferences.hashCode());
		result = prime * result + ((cookingTime == null) ? 0 : cookingTime.hashCode());
		result = prime * result + ((cookingUtensil == null) ? 0 : cookingUtensil.hashCode());
		result = prime * result + ((preference == null) ? 0 : preference.hashCode());
		result = prime * result * numberOfPeople.value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		Settings other = (Settings) obj;
		if (casseroleSettings != other.casseroleSettings
				|| !cookingPreferences.equals(other.cookingPreferences)
				|| !cookingTime.equals(other.cookingTime)
				|| !cookingUtensil.equals(other.cookingUtensil)
				|| preference != other.preference
				|| !numberOfPeople.equals(other.numberOfPeople)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Settings [casseroleSettings=" + casseroleSettings + ", preference=" + preference
				+ ", cookingPreferences=" + cookingPreferences + ", cookingTime=" + cookingTime
				+ ", cookingUtensil=" + cookingUtensil
				+ ", numberOfPeople=" + numberOfPeople + "]";
	}
}