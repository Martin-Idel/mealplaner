// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3FromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalV3ToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonBaseFunctions.getProposal1;
import static testcommons.CommonBaseFunctions.getProposal2;

import org.junit.jupiter.api.Test;

import mealplaner.model.proposal.Proposal;

class ProposalAdapterTest {

  @Test
  void adapterTest() {
    Proposal proposal1 = getProposal1();
    Proposal proposal2 = getProposal2();

    Proposal convertedProposal1 = convertProposalV3FromXml(convertProposalV3ToXml(proposal1));
    Proposal convertedProposal2 = convertProposalV3FromXml(convertProposalV3ToXml(proposal2));

    assertThat(convertedProposal1).isEqualTo(proposal1);
    assertThat(convertedProposal2).isEqualTo(proposal2);
  }
}
