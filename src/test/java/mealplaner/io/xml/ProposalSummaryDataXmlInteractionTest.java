// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;
import static testcommons.CommonFunctions.setupMealplanerDataWithAllMealsAndIngredients;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mealplaner.PluginStore;
import mealplaner.model.MealplanerData;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.model.settings.Settings;
import testcommons.XmlInteraction;

public class ProposalSummaryDataXmlInteractionTest extends XmlInteraction {
  private static final String RESOURCE_FILE_WITH_THREE_MEALS_V2 = "src/test/resources/proposalSummaryXmlV2.xml";

  @Test
  public void loadingProposalSummaryWorksCorrectlyForV2() {
    loadingProposalSummaryWorksCorrectlyFor(RESOURCE_FILE_WITH_THREE_MEALS_V2);
  }

  @Test
  public void roundTripResultsInSameOutputAsInput() {
    Map<DayOfWeek, Settings> defaultSettings = createDefaultSettings();
    Proposal proposal = getProposal1();
    LocalDate time = LocalDate.of(2017, 5, 3);
    MealplanerData mealPlan = setupMealplanerDataWithAllMealsAndIngredients();
    mealPlan.setTime(time);
    mealPlan.setDefaultSettings(DefaultSettings.from(defaultSettings));
    mealPlan.setLastProposal(proposal);

    ProposalSummaryDataWriter.saveXml(mealPlan, DESTINATION_FILE_PATH, new PluginStore());
    ProposalSummaryModel roundTripModel = ProposalSummaryDataReader
        .loadXml(MealplanerData.getInstance(), DESTINATION_FILE_PATH, new PluginStore());

    assertThat(roundTripModel.lastProposal).isEqualTo(proposal);
    assertThat(roundTripModel.time).isEqualTo(time);
    assertThat(roundTripModel.defaultSettings.getDefaultSettings())
        .containsAllEntriesOf(defaultSettings);
  }

  private void loadingProposalSummaryWorksCorrectlyFor(String filename) {
    Proposal proposal = getProposal1();
    LocalDate time = LocalDate.of(2017, 5, 3);
    loadFileWithName(filename);
    MealplanerData mealPlan = setupMealplanerDataWithAllMealsAndIngredients();

    ProposalSummaryModel loadedProposalSummaryData = ProposalSummaryDataReader
        .loadXml(mealPlan, DESTINATION_FILE_PATH, new PluginStore());

    assertThat(loadedProposalSummaryData.lastProposal).isEqualTo(proposal);
    assertThat(loadedProposalSummaryData.time).isEqualTo(time);
    Map<DayOfWeek, Settings> defaultSettings = createDefaultSettings();
    assertThat(loadedProposalSummaryData.defaultSettings.getDefaultSettings())
        .containsAllEntriesOf(defaultSettings);
  }

  private Map<DayOfWeek, Settings> createDefaultSettings() {
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(DayOfWeek.MONDAY, getSettings1());
    defaultSettings.put(DayOfWeek.WEDNESDAY, getSettings2());
    defaultSettings.put(DayOfWeek.FRIDAY, getSettings1());
    return defaultSettings;
  }
}
