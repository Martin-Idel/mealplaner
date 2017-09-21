package mealplaner.model.enums;

import static mealplaner.model.enums.PreferenceSettings.getPreferenceSettingsStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class PreferenceSettingsTest {

	@Test
	public void allPreferenceSettingsHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(PreferenceSettings.values(),
				bundles -> getPreferenceSettingsStrings(bundles));
	}

}
