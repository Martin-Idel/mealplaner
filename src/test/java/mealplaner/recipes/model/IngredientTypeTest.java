package mealplaner.recipes.model;

import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

import mealplaner.recipes.model.IngredientType;

public class IngredientTypeTest {

	@Test
	public void allIngredientTypesHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(IngredientType.values(),
				bundles -> IngredientType.getIngredientTypeStrings(bundles));
	}
}
