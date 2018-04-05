package mealplaner.xml.adapters;

import static java.util.Optional.ofNullable;
import static mealplaner.commons.NonnegativeInteger.nonNegative;
import static mealplaner.model.ProposedMenu.proposed;

import mealplaner.model.ProposedMenu;
import mealplaner.xml.model.v2.ProposedMenuXml;

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
