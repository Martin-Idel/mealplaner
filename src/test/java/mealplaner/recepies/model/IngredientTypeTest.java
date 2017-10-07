package mealplaner.recepies.model;

import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class IngredientTypeTest {

	@Test
	public void allIngredientTypesHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(IngredientType.values(),
				bundles -> IngredientType.getIngredientTypeStrings(bundles));
	}
}
