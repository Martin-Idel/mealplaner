// SPDX-License-Identifier: MIT

package mealplaner.api;

import java.util.Collection;
import java.util.List;

import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.ProposalBuilderStep;

public interface ProposalBuilderFactory {
  ProposalBuilderInterface createProposalBuilder(
      List<Meal> meals, Collection<ProposalBuilderStep> proposalBuilderSteps);
}
