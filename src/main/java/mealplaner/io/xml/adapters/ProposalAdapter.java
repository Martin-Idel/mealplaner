// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toList;

import java.util.List;

import mealplaner.io.xml.model.v2.ProposalXml;
import mealplaner.io.xml.model.v2.ProposedMenuXml;
import mealplaner.model.proposal.Proposal;

public final class ProposalAdapter {
  private ProposalAdapter() {
  }

  public static ProposalXml convertProposalV2ToXml(Proposal proposal) {
    List<ProposedMenuXml> mealList = proposal.getProposalList()
        .stream()
        .map(ProposedMenuAdapter::convertProposedMenuV2ToXml)
        .collect(toList());

    return new ProposalXml(mealList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static mealplaner.io.xml.model.v3.ProposalXml convertProposalV3ToXml(Proposal proposal) {
    List<mealplaner.io.xml.model.v3.ProposedMenuXml> mealList = proposal.getProposalList()
        .stream()
        .map(ProposedMenuAdapter::convertProposedMenuV3ToXml)
        .collect(toList());

    return new mealplaner.io.xml.model.v3.ProposalXml(mealList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static Proposal convertProposalV2FromXml(ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(ProposedMenuAdapter::convertProposedMenuV2FromXml)
            .collect(toList()),
        proposalData.date);
  }

  public static Proposal convertProposalV3FromXml(mealplaner.io.xml.model.v3.ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(ProposedMenuAdapter::convertProposedMenuV3FromXml)
            .collect(toList()),
        proposalData.date);
  }
}
