// SPDX-License-Identifier: MIT

package mealplaner.model.proposal;

import static java.time.LocalDate.now;
import static java.util.Collections.unmodifiableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Proposal {
  private final List<ProposedMenu> mealList;
  private final LocalDate date;
  private final boolean includeToday;

  private Proposal(List<ProposedMenu> proposedMenu, LocalDate date, boolean includeToday) {
    this.mealList = proposedMenu;
    this.date = date;
    this.includeToday = includeToday;
  }

  public static Proposal createProposal() {
    return new Proposal(new ArrayList<>(), now(), false);
  }

  public static Proposal from(boolean includeToday, List<ProposedMenu> proposedMenu) {
    return new Proposal(proposedMenu, now(), includeToday);
  }

  public static Proposal from(boolean includeToday, List<ProposedMenu> proposedMenu,
      LocalDate date) {
    return new Proposal(proposedMenu, date, includeToday);
  }

  public List<ProposedMenu> getProposalList() {
    return unmodifiableList(mealList);
  }

  public int getSize() {
    return mealList.size();
  }

  public ProposedMenu getItem(int index) {
    return mealList.get(index);
  }

  public boolean isToday() {
    return includeToday;
  }

  public LocalDate getTime() {
    return date;
  }

  public LocalDate getDateOfFirstProposedItem() {
    return includeToday ? date : date.plusDays(1);
  }

  @Override
  public String toString() {
    return "Proposal [mealList=" + mealList
        + ", calendar=" + date
        + ", includeToday=" + includeToday + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + date.hashCode();
    result = prime * result + (includeToday ? 1231 : 1237);
    result = prime * result + mealList.hashCode();
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
    Proposal other = (Proposal) obj;
    return date.equals(other.date)
        && includeToday == other.includeToday
        && mealList.equals(other.mealList);
  }
}