package mealplaner.model;

import static mealplaner.model.Proposal.getFromXml;
import static mealplaner.model.Proposal.writeProposal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;

import java.time.LocalDate;
import java.util.ArrayList;
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
		LocalDate date = LocalDate.of(2017, 7, 5);
		Proposal expected = new Proposal(meals, date, true);
		Document saveFileContent = createDocument();

		Proposal actual = getFromXml(writeProposal(saveFileContent, expected, "proposal"));

		assertThat(actual).isEqualTo(expected);
	}
}
