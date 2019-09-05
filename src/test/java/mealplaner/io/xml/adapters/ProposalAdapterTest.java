// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV2FromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV2ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getProposal1;

import org.junit.Test;

import mealplaner.model.proposal.Proposal;
import testcommons.CommonFunctions;

public class ProposalAdapterTest {

  @Test
  public void adapterTest() {
    Proposal proposal1 = getProposal1();
    Proposal proposal2 = CommonFunctions.getProposal2();

    Proposal convertedProposal1 = convertProposalV2FromXml(convertProposalV2ToXml(proposal1));
    Proposal convertedProposal2 = convertProposalV2FromXml(convertProposalV2ToXml(proposal2));

    assertThat(convertedProposal1).isEqualTo(proposal1);
    assertThat(convertedProposal2).isEqualTo(proposal2);
  }
}
