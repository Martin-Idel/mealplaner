// SPDX-License-Identifier: MIT

package mealplaner.api;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;

public interface ProposalBuilderInterface {
  ProposalBuilderInterface randomise(boolean randomise);

  ProposalBuilderInterface firstProposal(boolean today);

  Proposal propose(Settings[] settings);
}
