package mealplaner.model.proposal;

import mealplaner.model.meal.enums.Sidedish;

public class SideDish {

  public Sidedish current;
  public int inARow;

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

}
