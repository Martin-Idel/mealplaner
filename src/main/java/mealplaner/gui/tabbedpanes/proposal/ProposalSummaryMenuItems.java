package mealplaner.gui.tabbedpanes.proposal;

import static mealplaner.commons.BundleStore.BUNDLES;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import mealplaner.commons.gui.JMenuBuilder;

public final class ProposalSummaryMenuItems {
  private ProposalSummaryMenuItems() {
  }

  public static JMenuItem printProposalMenu(ActionListener listener) {
    return JMenuBuilder.builder("PrintProposal")
        .addLabelText(BUNDLES.message("menuDataPrintProposal"))
        .addMnemonic(BUNDLES.message("menuDataPrintProposalMnemonic"))
        .addActionListener(listener)
        .build();
  }

  public static JMenuItem viewProposalMenu(ActionListener listener) {
    return JMenuBuilder.builder("LastProposal")
        .addLabelText(BUNDLES.message("menuDataLastProposal"))
        .addMnemonic(BUNDLES.message("menuDataLastProposalMnemonic"))
        .addActionListener(listener)
        .build();
  }
}
