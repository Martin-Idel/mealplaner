// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;
import java.util.UUID;

import mealplaner.commons.NonnegativeInteger;

public final class ProposedMenu {
  public final Optional<UUID> entry;
  public final UUID main;
  public final Optional<UUID> desert;
  public final NonnegativeInteger numberOfPeople;

  private ProposedMenu(Optional<UUID> entry, UUID main, Optional<UUID> desert,
      NonnegativeInteger numberOfPeople) {
    this.entry = entry;
    this.main = main;
    this.desert = desert;
    this.numberOfPeople = numberOfPeople;
  }

  public static ProposedMenu proposed(Optional<UUID> entry, UUID main, Optional<UUID> desert,
      NonnegativeInteger numberOfPeople) {
    return new ProposedMenu(entry, main, desert, numberOfPeople);
  }

  public static ProposedMenu mainOnly(UUID main, NonnegativeInteger numberOfPeople) {
    return new ProposedMenu(empty(), main, empty(), numberOfPeople);
  }

  public static ProposedMenu entryAndMain(UUID entry,
      UUID main,
      NonnegativeInteger numberOfPeople) {
    return new ProposedMenu(of(entry), main, empty(), numberOfPeople);
  }

  public static ProposedMenu mainAndDesert(UUID main,
      UUID desert,
      NonnegativeInteger numberOfPeople) {
    return new ProposedMenu(empty(), main, of(desert), numberOfPeople);
  }

  public static ProposedMenu threeCourseMeal(UUID entry,
      UUID main,
      UUID desert,
      NonnegativeInteger numberOfPeople) {
    return new ProposedMenu(of(entry), main, of(desert), numberOfPeople);
  }

  @Override
  public String toString() {
    return "ProposedMenu{entry=" + entry + ", main=" + main + ", desert=" + desert + " for="
        + numberOfPeople + "}";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + desert.hashCode();
    result = prime * result + entry.hashCode();
    result = prime * result + main.hashCode();
    result = prime * result + numberOfPeople.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ProposedMenu other = (ProposedMenu) obj;
    return entry.equals(other.entry)
        && main.equals(other.main)
        && desert.equals(other.desert)
        && numberOfPeople.equals(other.numberOfPeople);
  }
}
