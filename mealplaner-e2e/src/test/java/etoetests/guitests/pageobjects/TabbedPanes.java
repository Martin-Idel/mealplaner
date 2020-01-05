// SPDX-License-Identifier: MIT

package etoetests.guitests.pageobjects;

public enum TabbedPanes {
  PROPOSAL_SUMMARY(0), DATABASE_EDIT(1), INGREDIENTS_EDIT(2);

  private final int number;

  TabbedPanes(int number) {
    this.number = number;
  }

  public int number() {
    return number;
  }
}
