// SPDX-License-Identifier: MIT

package etoetests.xmlsmoketests;

import static mealplaner.io.xml.ProposalSummaryDataReader.loadXml;
import static org.assertj.core.api.Assertions.assertThat;
import static testcommons.CommonFunctions.getProposal1;
import static testcommons.CommonFunctions.getSettings1;
import static testcommons.CommonFunctions.getSettings2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import mealplaner.Kochplaner;
import mealplaner.io.xml.ProposalSummaryModel;
import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.Settings;
import testcommons.XmlInteraction;

public class ProposalSummaryDataXmlInteractionTest extends XmlInteraction {
  private static final String RESOURCE_FILE_WITH_PROPOSAL_V3 = "src/test/resources/proposalSummaryXmlV3.xml";

  @Test
  public void loadingProposalSummaryWorksCorrectlyForV3() {
    Proposal proposal = getProposal1();
    LocalDate time = LocalDate.of(2017, 5, 3);
    loadFileWithName(RESOURCE_FILE_WITH_PROPOSAL_V3);

    ProposalSummaryModel loadedProposalSummaryData = loadXml(DESTINATION_FILE_PATH, Kochplaner.registerPlugins());

    assertThat(loadedProposalSummaryData.lastProposal).isEqualTo(proposal);
    assertThat(loadedProposalSummaryData.time).isEqualTo(time);
    Map<DayOfWeek, Settings> defaultSettings = new HashMap<>();
    defaultSettings.put(DayOfWeek.MONDAY, getSettings1());
    defaultSettings.put(DayOfWeek.WEDNESDAY, getSettings2());
    defaultSettings.put(DayOfWeek.FRIDAY, getSettings1());
    assertThat(loadedProposalSummaryData.defaultSettings.getDefaultSettings())
        .containsAllEntriesOf(defaultSettings);
  }
}
