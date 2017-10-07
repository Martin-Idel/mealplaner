package mealplaner.recepies.model;

import static testcommons.CommonFunctions.allEnumValuesHaveACorrespondingStringRepresentation;

import org.junit.Test;

public class MeasureTest {

	@Test
	public void allMeasuresHaveACorrespondingStringRepresentation() {
		allEnumValuesHaveACorrespondingStringRepresentation(Measure.values(),
				bundles -> Measure.getMeasureStrings(bundles));
	}
}
