package mealplaner.model.enums;

import static mealplaner.model.enums.CookingPreference.getCookingPreferenceStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class CookingPrefernceTest {

	@Test
	public void allCookingPreferncesHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(CookingPreference.values(),
				bundles -> getCookingPreferenceStrings(bundles));
	}

}
