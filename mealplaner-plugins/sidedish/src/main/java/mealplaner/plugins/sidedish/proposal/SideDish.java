// SPDX-License-Identifier: MIT

package mealplaner.plugins.sidedish.proposal;

import mealplaner.plugins.sidedish.mealextension.Sidedish;

public class SideDish {
  private Sidedish current;
  private int inARow;

  public SideDish() {
    current = Sidedish.NONE;
    inARow = 1;
  }

  public void reset() {
    current = Sidedish.NONE;
    inARow = 1;
  }

  public SideDish resetToSideDish(Sidedish newSidedish) {
    current = newSidedish;
    inARow = 1;
    return this;
  }

  public SideDish incrementInARow() {
    inARow++;
    return this;
  }

  public Sidedish getCurrent() {
    return current;
  }

  public void setCurrent(Sidedish current) {
    this.current = current;
  }

  public int getInARow() {
    return inARow;
  }
}
