package mealplaner.model.enums;

import static mealplaner.model.enums.ObligatoryUtensil.getObligatoryUtensilStrings;
import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class ObligatoryUtensilTest {

	@Test
	public void allObligatoryUtensilsHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(ObligatoryUtensil.values(),
				bundles -> getObligatoryUtensilStrings(bundles));
	}

}
