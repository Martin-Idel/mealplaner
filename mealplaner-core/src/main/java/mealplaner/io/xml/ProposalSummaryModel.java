// SPDX-License-Identifier: MIT

package mealplaner.io.xml;

import static java.time.LocalDate.now;
import static mealplaner.model.proposal.Proposal.createProposal;
import static mealplaner.model.settings.DefaultSettings.createDefaultSettings;

import java.time.LocalDate;

import mealplaner.model.proposal.Proposal;
import mealplaner.model.settings.DefaultSettings;
import mealplaner.plugins.PluginStore;

public class ProposalSummaryModel {
  public final Proposal lastProposal;
  public final DefaultSettings defaultSettings;
  public final LocalDate time;

  public ProposalSummaryModel(PluginStore pluginStore) {
    this.lastProposal = createProposal();
    this.defaultSettings = createDefaultSettings(pluginStore);
    this.time = now();
  }

  public ProposalSummaryModel(Proposal lastProposal, DefaultSettings defaultSettings,
      LocalDate time) {
    this.lastProposal = lastProposal;
    this.defaultSettings = defaultSettings;
    this.time = time;
  }
}
