// SPDX-License-Identifier: MIT

package mealplaner.io.xml.adapters;

import static java.util.stream.Collectors.toList;
import static mealplaner.io.xml.adapters.ProposedMenuAdapter.convertProposedMenuFromXml;
import static mealplaner.io.xml.adapters.ProposedMenuAdapter.convertProposedMenuToXml;

import java.util.List;

import mealplaner.io.xml.model.v2.ProposalXml;
import mealplaner.io.xml.model.v2.ProposedMenuXml;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;

public final class ProposalAdapter {
  private ProposalAdapter() {
  }

  public static ProposalXml convertProposalToXml(Proposal proposal) {
    List<ProposedMenuXml> mealList = proposal.getProposalList()
        .stream()
        .map(meal -> convertProposedMenuToXml(meal))
        .collect(toList());

    return new ProposalXml(mealList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static Proposal convertProposalFromXml(MealplanerData data, ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(meal -> convertProposedMenuFromXml(meal))
            .collect(toList()),
        proposalData.date);
  }
}
