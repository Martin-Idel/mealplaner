package guittests.helpers;

public enum TabbedPanes {
  PROPOSAL_SUMMARY(0), DATABASE_EDIT(1);

  private final int number;

  private TabbedPanes(int number) {
    this.number = number;
  }

  public int number() {
    return number;
  }
}
