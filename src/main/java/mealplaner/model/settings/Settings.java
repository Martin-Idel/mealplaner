package mealplaner.model.settings;

import static mealplaner.io.XMLHelpers.createTextNode;
import static mealplaner.io.XMLHelpers.readBoolean;
import static mealplaner.io.XMLHelpers.readEnum;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;

public class Settings implements Serializable {

	private static final long serialVersionUID = 1L;

	private final CasseroleSettings casseroleSettings;
	private final PreferenceSettings preference;
	private final CookingPreferenceSetting cookingPreferences;
	private final CookingTimeSetting cookingTime;
	private final CookingUtensilSetting cookingUtensil;

	public Settings() {
		this(new CookingTimeSetting(), false, CasseroleSettings.POSSIBLE,
				PreferenceSettings.NORMAL);
	}

	public Settings(CookingTimeSetting cookingTime,
			boolean manyPeople,
			CasseroleSettings casseroleSettings,
			PreferenceSettings preferenceSettings) {

		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingTime = cookingTime;
		this.cookingUtensil = new CookingUtensilSetting();
		if (manyPeople) {
			this.cookingUtensil.setManyPeople();
		}
		this.casseroleSettings = casseroleSettings;
		this.cookingUtensil.setCasseroleSettings(casseroleSettings);

		this.preference = preferenceSettings;
		this.cookingPreferences.setCookingPreferences(preference);
	}

	public Settings(Settings setting) {
		this.casseroleSettings = setting.getCasserole();
		this.preference = setting.getPreference();
		this.cookingPreferences = new CookingPreferenceSetting();
		this.cookingPreferences.setCookingPreferences(this.preference);
		this.cookingTime = new CookingTimeSetting(setting.getCookingTime());
		this.cookingUtensil = new CookingUtensilSetting(setting.getCookingUtensil());
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

	public static Element generateXml(Document saveFileContent, Settings settings, int dayOfWeek) {
		Element settingsNode = saveFileContent.createElement("setting");
		settingsNode.setAttribute("dayOfWeek", Integer.toString(dayOfWeek));

		settingsNode.appendChild(createTextNode(saveFileContent,
				"casseroleSettings",
				() -> settings.getCasserole().name()));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"preferenceSettings",
				() -> settings.getPreference().name()));
		settingsNode.appendChild(createTextNode(saveFileContent,
				"manyPeople",
				() -> Boolean.toString(
						settings.getCookingUtensil().contains(ObligatoryUtensil.PAN))));
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
		boolean manyPeople = readBoolean(false, currentSetting, "manyPeople");
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
		return new Settings(cookingTimes, manyPeople, casseroleSettings, preferenceSetting);
	}
}