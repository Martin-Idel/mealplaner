// SPDX-License-Identifier: MIT

package mealplaner.gui.tabbedpanes.proposal;

import static mealplaner.commons.BundleStore.BUNDLES;
import static mealplaner.commons.gui.HelpPanel.mealPlanerHelpScrollPane;
import static mealplaner.commons.gui.JMenuBuilder.builder;

import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import mealplaner.commons.gui.JMenuBuilder;

final class ProposalSummaryMenuItems {
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

  public static JMenuItem helpMenu(JFrame frame) {
    return builder("HelpProposal").addLabelText(BUNDLES.message("menuHelpProposal"))
        .addMnemonic(BUNDLES.message("menuHelpProposalMnemonic"))
        .addActionListener(action -> JOptionPane.showMessageDialog(frame,
            mealPlanerHelpScrollPane("ProposalEditHelp"),
            BUNDLES.message("menuHelp"), JOptionPane.PLAIN_MESSAGE))
        .build();
  }
}
