package mealplaner.model;

import static mealplaner.model.Proposal.readProposal;
import static mealplaner.model.Proposal.writeProposal;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.createDocument;
import static testcommons.CommonFunctions.getProposal1;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;

public class ProposalTest {

  @Test
  public void test() throws ParserConfigurationException {
    Proposal expected = getProposal1();
    Document saveFileContent = createDocument();

    Proposal actual = readProposal(writeProposal(saveFileContent, expected, "proposal"));

    assertThat(actual).isEqualTo(expected);
  }
}
