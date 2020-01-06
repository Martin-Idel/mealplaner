// SPDX-License-Identifier: MIT

package mealplaner.proposal;

import java.util.Collection;
import java.util.List;

import mealplaner.api.ProposalBuilderFactory;
import mealplaner.api.ProposalBuilderInterface;
import mealplaner.model.meal.Meal;
import mealplaner.plugins.api.ProposalBuilderStep;

public class ProposalBuilderFactoryImpl implements ProposalBuilderFactory {
  @Override
  public ProposalBuilderInterface createProposalBuilder(
      List<Meal> meals, Collection<ProposalBuilderStep> proposalBuilderSteps) {
    return new ProposalBuilder(meals, proposalBuilderSteps);
  }
}
