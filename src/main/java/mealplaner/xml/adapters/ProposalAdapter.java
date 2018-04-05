package mealplaner.xml.adapters;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.adapters.ProposedMenuAdapter.convertProposedMenuFromXml;
import static mealplaner.xml.adapters.ProposedMenuAdapter.convertProposedMenuToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsFromXml;

import java.util.List;

import mealplaner.MealplanerData;
import mealplaner.model.Proposal;
import mealplaner.xml.model.v2.ProposalXml;
import mealplaner.xml.model.v2.ProposedMenuXml;

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

  public static Proposal convertProposalFromXml(MealplanerData data,
      mealplaner.xml.model.v1.ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(meal -> convertMealFromXml(data, meal))
            .collect(toList()),
        proposalData.settingsList
            .stream()
            .map(setting -> convertSettingsFromXml(setting))
            .collect(toList()),
        proposalData.date);
  }
}
