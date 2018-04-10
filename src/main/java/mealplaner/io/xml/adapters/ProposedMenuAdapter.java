package mealplaner.io.xml.adapters;

import static java.util.Optional.ofNullable;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.proposal.ProposedMenu.proposed;

import mealplaner.io.xml.model.v2.ProposedMenuXml;
import mealplaner.model.proposal.ProposedMenu;

public final class ProposedMenuAdapter {
  private ProposedMenuAdapter() {
  }

  public static ProposedMenuXml convertProposedMenuToXml(ProposedMenu proposedMenu) {
    return new ProposedMenuXml(proposedMenu.entry.isPresent() ? proposedMenu.entry.get() : null,
        proposedMenu.main,
        proposedMenu.desert.isPresent() ? proposedMenu.desert.get() : null,
        proposedMenu.numberOfPeople.value);
  }

  public static ProposedMenu convertProposedMenuFromXml(ProposedMenuXml proposedMenu) {
    return proposed(ofNullable(proposedMenu.entry), proposedMenu.main,
        ofNullable(proposedMenu.desert),
        nonNegative(proposedMenu.numberOfPeople));
  }
}