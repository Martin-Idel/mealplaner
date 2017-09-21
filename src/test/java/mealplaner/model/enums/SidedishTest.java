package mealplaner.model.enums;

import static mealplaner.model.enums.Sidedish.getSidedishStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class SidedishTest {

	@Test
	public void allSidedishsHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(Sidedish.values(),
				bundles -> getSidedishStrings(bundles));
	}

}
