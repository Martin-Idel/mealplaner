package mealplaner.recipes.model;

import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

import mealplaner.recipes.model.Measure;

public class MeasureTest {

	@Test
	public void allMeasuresHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(Measure.values(),
				bundles -> Measure.getMeasureStrings(bundles));
	}
}
