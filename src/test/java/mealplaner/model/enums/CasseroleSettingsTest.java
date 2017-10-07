package mealplaner.model.enums;

import static mealplaner.model.enums.CasseroleSettings.getCasseroleSettingsStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class CasseroleSettingsTest {

	@Test
	public void allCasseroleSettingsHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(CasseroleSettings.values(),
				bundles -> getCasseroleSettingsStrings(bundles));
	}

}
