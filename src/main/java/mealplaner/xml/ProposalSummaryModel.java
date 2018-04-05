package mealplaner.xml;

import static java.time.LocalDate.MIN;
import static mealplaner.model.Proposal.createProposal;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;

import java.time.LocalDate;

import mealplaner.model.Proposal;
import mealplaner.model.settings.DefaultSettings;

public class ProposalSummaryModel {
  public final Proposal lastProposal;
  public final DefaultSettings defaultSettings;
  public final LocalDate time;

  public ProposalSummaryModel() {
    this.lastProposal = createProposal();
    this.defaultSettings = createDefaultSettings();
    this.time = MIN;
  }

  public ProposalSummaryModel(Proposal lastProposal, DefaultSettings defaultSettings,
      LocalDate time) {
    this.lastProposal = lastProposal;
    this.defaultSettings = defaultSettings;
    this.time = time;
  }
}
