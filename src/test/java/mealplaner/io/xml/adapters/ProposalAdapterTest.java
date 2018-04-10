package mealplaner.io.xml.adapters;

import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalFromXml;
import static mealplaner.io.xml.adapters.ProposalAdapter.convertProposalToXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllMealsAndIngredients;

import org.junit.Test;

import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import testcommons.CommonFunctions;

public class ProposalAdapterTest {

  @Test
  public void adapterTest() {
    MealplanerData mealPlan = setupMealplanerDataWithAllMealsAndIngredients();
    Proposal proposal1 = getProposal1();
    Proposal proposal2 = CommonFunctions.getProposal2();

    Proposal convertedProposal1 = convertProposalFromXml(mealPlan, convertProposalToXml(proposal1));
    Proposal convertedProposal2 = convertProposalFromXml(mealPlan, convertProposalToXml(proposal2));

    assertThat(convertedProposal1).isEqualTo(proposal1);
    assertThat(convertedProposal2).isEqualTo(proposal2);
  }
}
