package mealplaner.model.enums;

import static mealplaner.model.enums.CookingTime.getCookingTimeStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class CookingTimeTest {

	@Test
	public void allCookingTimesHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(CookingTime.values(),
				bundles -> getCookingTimeStrings(bundles));
	}

}
