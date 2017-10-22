package mealplaner.model.settings;

import static java.time.DayOfWeek.valueOf;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;
import static mealplaner.io.XMLHelpers.logEmptyOptional;
import static mealplaner.io.XMLHelpers.logFailedXmlRetrieval;
import static mealplaner.model.settings.Settings.copySettings;
import static mealplaner.model.settings.Settings.defaultSetting;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DefaultSettings {
	private static final List<DayOfWeek> DAYS_OF_WEEK = Arrays.asList(DayOfWeek.values());
	private final Map<DayOfWeek, Settings> defaultSettings;

	private DefaultSettings(Map<DayOfWeek, Settings> defaultSettings) {
		this.defaultSettings = defaultSettings;
		DAYS_OF_WEEK.stream().forEach(dayOfWeek -> this.defaultSettings
				.computeIfAbsent(dayOfWeek, day -> defaultSetting()));
	}

	public static DefaultSettings fromMap(Map<DayOfWeek, Settings> defaultSettings) {
		return new DefaultSettings(defaultSettings);
	}

	public static DefaultSettings defaultSettings() {
		return new DefaultSettings(new HashMap<>());
	}

	public static DefaultSettings copy(DefaultSettings defaultSettings) {
		return new DefaultSettings(copyHashMap(defaultSettings.defaultSettings));
	}

	private static Map<DayOfWeek, Settings> copyHashMap(Map<DayOfWeek, Settings> defaultSettings) {
		return defaultSettings.entrySet().stream()
				.collect(toMap(entry -> entry.getKey(), entry -> copySettings(entry.getValue())));
	}

	public Map<DayOfWeek, Settings> getDefaultSettings() {
		return copyHashMap(defaultSettings);
	}

	public static DefaultSettings parseDefaultSettings(Element defaultSettingsNode) {
		NodeList settingsList = defaultSettingsNode.getElementsByTagName("setting");
		Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
		for (int i = 0; i < settingsList.getLength(); i++) {
			Node dayOfWeekNode = settingsList.item(i).getAttributes().getNamedItem("dayOfWeek");
			Optional<DayOfWeek> key = dayOfWeekNode != null
					? of(valueOf(dayOfWeekNode.getTextContent()))
					: empty();
			if (key.isPresent()) {
				defaultSettings.put(key.get(),
						settingsList.item(i).getNodeType() == Node.ELEMENT_NODE
								? Settings.loadFromXml((Element) settingsList.item(i))
								: logFailedXmlRetrieval(defaultSetting(), "Settings " + key.get(),
										defaultSettingsNode));
			} else {
				logEmptyOptional(key, "Day of week " + i, defaultSettingsNode);
			}
		}
		return fromMap(defaultSettings);
	}

	public static Element writeDefaultSettings(Document saveFileContent,
			DefaultSettings defaultSettings, String nodeName) {
		Element defaultSettingsNode = saveFileContent.createElement(nodeName);
		for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
			defaultSettingsNode.appendChild(Settings.generateXml(saveFileContent,
					defaultSettings.defaultSettings.get(dayOfWeek), dayOfWeek, "setting"));
		}
		return defaultSettingsNode;
	}

	@Override
	public String toString() {
		return "[" + DAYS_OF_WEEK.stream()
				.map(day -> day.toString() + " " + defaultSettings.get(day).toString())
				.collect(joining(",")) + "]";
	}

	@Override
	public int hashCode() {
		return 31 + ((defaultSettings == null) ? 0 : defaultSettings.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		DefaultSettings other = (DefaultSettings) obj;
		if (!other.defaultSettings.equals(defaultSettings)) {
			return false;
		}
		return true;
	}
}
