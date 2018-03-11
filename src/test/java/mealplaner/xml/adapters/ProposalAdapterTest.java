package mealplaner.xml.adapters;

import static mealplaner.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.xml.adapters.ProposalAdapter.convertProposalToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.getProposal2;

import org.junit.Test;

import mealplaner.model.Proposal;

public class ProposalAdapterTest {

  @Test
  public void adapterTest() {
    Proposal proposal1 = getProposal1();
    Proposal proposal2 = getProposal2();

    Proposal convertedProposal1 = convertProposalFromXml(convertProposalToXml(proposal1));
    Proposal convertedProposal2 = convertProposalFromXml(convertProposalToXml(proposal2));

    assertThat(convertedProposal1).isEqualTo(proposal1);
    assertThat(convertedProposal2).isEqualTo(proposal2);
  }
}
