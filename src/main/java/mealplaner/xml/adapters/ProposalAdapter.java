package mealplaner.xml.adapters;

import static java.util.stream.Collectors.toList;
import static mealplaner.xml.adapters.MealAdapter.convertMealFromXml;
import static mealplaner.xml.adapters.MealAdapter.convertMealToXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsFromXml;
import static mealplaner.xml.adapters.SettingsAdapter.convertSettingsToXml;

import java.util.List;

import mealplaner.model.Proposal;
import mealplaner.xml.model.MealXml;
import mealplaner.xml.model.ProposalXml;
import mealplaner.xml.model.SettingsXml;

public final class ProposalAdapter {
  private ProposalAdapter() {
  }

  public static ProposalXml convertProposalToXml(Proposal proposal) {
    List<MealXml> mealList = proposal.getProposalList()
        .stream()
        .map(meal -> convertMealToXml(meal))
        .collect(toList());

    List<SettingsXml> settingsList = proposal.getSettingsList()
        .stream()
        .map(setting -> convertSettingsToXml(setting))
        .collect(toList());

    return new ProposalXml(mealList,
        settingsList,
        proposal.getDateOfFirstProposedItem(),
        proposal.isToday());
  }

  public static Proposal convertProposalFromXml(ProposalXml proposalData) {
    return Proposal.from(proposalData.includeToday,
        proposalData.mealList
            .stream()
            .map(meal -> convertMealFromXml(meal))
            .collect(toList()),
        proposalData.settingsList
            .stream()
            .map(setting -> convertSettingsFromXml(setting))
            .collect(toList()),
        proposalData.date);
  }
}
