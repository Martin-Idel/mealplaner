// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toList;

import java.util.List;

import mealplaner.io.xml.model.v3.ProposalXml;
import mealplaner.io.xml.model.v3.ProposedMenuXml;
import mealplaner.model.proposal.Proposal;

public final class ProposalAdapter {
  private ProposalAdapter() {
  }

  public static ProposalXml convertProposalV3ToXml(Proposal proposal) {
    List<ProposedMenuXml> mealList = proposal.getProposalList()
        .stream()
        .map(ProposedMenuAdapter::convertProposedMenuV3ToXml)
        .collect(toList());

    return new ProposalXml(mealList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static Proposal convertProposalV3FromXml(ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(ProposedMenuAdapter::convertProposedMenuV3FromXml)
            .collect(toList()),
        proposalData.date);
  }
}
