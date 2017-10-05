package testcommons;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.EnumMap;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import mealplaner.BundleStore;
import mealplaner.model.Meal;
import mealplaner.model.enums.CasseroleSettings;
import mealplaner.model.enums.CookingPreference;
import mealplaner.model.enums.CookingTime;
import mealplaner.model.enums.ObligatoryUtensil;
import mealplaner.model.enums.PreferenceSettings;
import mealplaner.model.enums.Sidedish;
import mealplaner.model.settings.CookingTimeSetting;
import mealplaner.model.settings.Settings;

public class CommonFunctions {
	public static <T extends Enum<T>> void allEnumValuesHaveACorrespondingStringRepresentation(
			T[] enumValues,
			Function<BundleStore, EnumMap<T, String>> getEnumStrings) {
		BundleStore bundles = mock(BundleStore.class);
		when(bundles.message(anyString())).thenReturn("");

		EnumMap<T, String> enumMap = getEnumStrings.apply(bundles);
		for (T setting : enumValues) {
			assertTrue(enumMap.containsKey(setting));
		}
	}

	public static Document createDocument() throws ParserConfigurationException {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = docFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	public static Meal getMeal1() {
		return new Meal("Test1", CookingTime.SHORT, Sidedish.PASTA,
				ObligatoryUtensil.PAN, CookingPreference.VERY_POPULAR, 5, "no comment");
	}

	public static Meal getMeal2() {
		return new Meal("Test2", CookingTime.SHORT, Sidedish.NONE,
				ObligatoryUtensil.POT, CookingPreference.NO_PREFERENCE, 1, "");
	}

	public static Settings getSettings1() {
		return new Settings(new CookingTimeSetting(CookingTime.VERY_SHORT), false,
				CasseroleSettings.NONE, PreferenceSettings.RARE_PREFERED);
	}
}
