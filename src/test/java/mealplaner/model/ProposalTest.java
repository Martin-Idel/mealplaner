package mealplaner.model;

import static mealplaner.model.Proposal.from;
import static mealplaner.model.Proposal.readProposal;
import static mealplaner.model.Proposal.writeProposal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getMeal1;
import static testcommons.CommonFunctions.getMeal2;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Rule;
import org.junit.Test;
import org.w3c.dom.Document;

import mealplaner.model.settings.Settings;
import testcommons.BundlesInitialization;

public class ProposalTest {
	@Rule
	public final BundlesInitialization bundlesInitialization = new BundlesInitialization();

	@Test
	public void test() throws ParserConfigurationException {
		List<Meal> meals = new ArrayList<>();
		meals.add(getMeal1());
		meals.add(getMeal2());
		List<Settings> settings = new ArrayList<>();
		settings.add(getSettings1());
		settings.add(getSettings2());
		LocalDate date = LocalDate.of(2017, 7, 5);
		Proposal expected = from(true, meals, settings, date);
		Document saveFileContent = createDocument();

		Proposal actual = readProposal(writeProposal(saveFileContent, expected, "proposal"));

		assertThat(actual).isEqualTo(expected);
	}
}
