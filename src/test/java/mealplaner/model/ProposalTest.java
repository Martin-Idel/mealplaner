package mealplaner.model;

import static mealplaner.model.Proposal.getFromXml;
import static mealplaner.model.Proposal.saveToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

public class ProposalTest {

	@Test
	public void test() throws ParserConfigurationException {
		List<Meal> meals = new ArrayList<>();
		meals.add(getMeal1());
		meals.add(getMeal2());
		Calendar cal = Calendar.getInstance();
		long calTime = 1000000000;
		cal.setTimeInMillis(calTime);
		Proposal expected = new Proposal(meals, cal, true);
		Document saveFileContent = createDocument();

		Proposal actual = getFromXml(saveToXml(saveFileContent, expected, "proposal"));

		assertThat(actual).isEqualTo(expected);
	}
}
